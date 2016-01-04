/*
 * Wazza
 * https://github.com/Wazzaio/wazza
 * Copyright (C) 2013-2015  Duarte Barbosa, João Vazão Vasques
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package controllers.api

import com.google.inject._
import play.api._
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.{JsArray, Json, JsValue}
import play.api.mvc._
import service.security.definitions.TokenManagerService
import service.user.definitions._
import scala.concurrent._
import ExecutionContext.Implicits.global
import controllers.security._
import user._
import user.messages._
import scala.collection.mutable.Stack
import scala.util.{Try, Success, Failure}
import models.payments.{PurchaseInfo}
import payments.paypal._
import models.application._
import application._
import application.messages._
import akka.pattern.ask
import scala.concurrent.duration._
import akka.util.{Timeout}
import scala.collection.mutable.Stack

class PurchaseController @Inject()(
  paypalService: PayPalService
) extends Controller {

  def handlePurchase() = ApiSecurityAction.async(parse.json) {implicit request =>
    val companyName = request.companyName
    val applicationName = request.applicationName
    
    try {
      val content = request.body.as[JsValue]
      val userProxy = UserProxy.getInstance()
      val req = new PRSave(new Stack, companyName, applicationName, content)
      userProxy ! req
      Future.successful(Ok)
    } catch {
      case _: Throwable => {
        Future.successful(BadRequest(Json.obj("error" -> "Invalid purchase json description")))
      }
    }
  }

  private def getPayPalCredentials(companyName: String, applicationName: String): Future[PayPalCredentials] = {
    implicit val timeout = Timeout(10 seconds)
    val appProxy = ApplicationProxy.getInstance()
      (appProxy ? new ARFind(new Stack, companyName, applicationName, true)).mapTo[AROptionResponse] map {optApp =>
      optApp.res match {
        case Some(app) => app.paypalCredentials.getOrElse(throw new Exception("Credentials not found"))
        case _ => throw new Exception("Credentials not found")
      }
    }
  }

  def verifyPayment() = ApiSecurityAction.async(parse.json) {implicit request =>
    val paymentID = (request.body \ "responseID").as[String]
    val amount = (request.body \ "price").as[Double]
    val currency = (request.body \ "currencyCode").as[String]

    try {
      for {
        credentials <- getPayPalCredentials(request.companyName, request.applicationName)
        token <- paypalService.getAccessToken(credentials.clientId, credentials.secret)
        valid <- paypalService.verifyPayment(token, paymentID, amount, currency)
      } yield Ok(Json.obj("result" -> valid))
    } catch {
      case _: Exception => Future.successful(NotFound(Json.obj("error" -> "PayPal credentials not found")))
    }
  }
}

