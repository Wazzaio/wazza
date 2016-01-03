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

package controllers.dashboard

import play.api._
import play.api.mvc._
import scala.concurrent._
import ExecutionContext.Implicits.global
import controllers.security._
import service.security.definitions.{TokenManagerService}
import com.google.inject._
import play.api.libs.json._
import models.application._
import java.util.Date
import akka.util.{Timeout}
import akka.pattern.ask
import user._
import user.messages._
import application._
import application.messages._
import scala.concurrent.duration._
import scala.collection.mutable.Stack
import payments.{PaymentTypes}

class SettingsController  extends Controller {

  private implicit val timeout = Timeout(10 seconds)
  private val userProxy = UserProxy.getInstance()
  private val appProxy = ApplicationProxy.getInstance()

	def bootstrap(appName: String) = UserAuthenticationAction.async {implicit request =>
    val appsFuture = (userProxy ? new URGetApplications(new Stack, request.userId, true)).mapTo[URApplicationsResponse]
    appsFuture flatMap {applications =>
      val userFuture = (userProxy ? new URFind(new Stack, request.userId, true)).mapTo[UROptionResponse]
		  userFuture flatMap {userOpt =>
				val user = userOpt.res.get
				val companyName = user.company
				val application = applications.res.find(_ == appName).get
        val futureApp = (appProxy ? new ARFind(new Stack, companyName, appName, true)).mapTo[AROptionResponse]
				val info = futureApp map {optApp =>
					(optApp.res map {application =>
						val result = Json.obj(
							"userInfo" -> Json.obj(
								"name" -> user.name,
								"email" -> user.email
								),
							"credentials" -> Json.obj(
								"sdkToken" -> application.credentials.sdkToken
								)
							)
            application.paypalCredentials match {
              case Some(credentials) => {
                result ++ Json.obj("payPalCredentials" -> Json.toJson(credentials))
              }
              case None => result
            }
						}).get
				}
				info map {Ok(_)}
			}
		}
	}

	def settings = UserAuthenticationAction {implicit request =>
		Ok(views.html.settings())
	}

  def updatePaymentsCredentialsController(
    companyName: String,
    applicationName: String
  ) = UserAuthenticationAction.async(parse.json) {implicit request =>
    try {
      (request.body \ "paymentSystem").as[Int] match {
        /** PayPal **/
        case 2 => {
          request.body.validate[PayPalCredentials] match {
            case credentials: JsSuccess[PayPalCredentials] => {
              appProxy ! new ARAddPayPalCredentials(new Stack, companyName, applicationName, credentials.get, true)
              appProxy ! new ARAddPaymentSystem(new Stack, companyName, applicationName, PaymentTypes.PayPal)
            }
            case _: JsError => Future.successful(BadRequest)
          }
        }
      }      

      Future.successful(Ok)
    } catch {
      case ex: Exception => Future.successful(BadRequest)
    }
  }
}

