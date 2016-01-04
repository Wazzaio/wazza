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

package controllers.security


import play.api._
import play.api.mvc._
import play.api.cache._
import play.api.libs.json._
import play.api.data._
import play.api.mvc.Results._
import play.api.mvc.BodyParsers.parse
import java.security.SecureRandom
import scala.annotation.tailrec
import com.google.inject._
import service.security.implementations._
import service.persistence.implementations._
import scala.concurrent._
import ExecutionContext.Implicits.global
import play.api.Logger
import service.persistence.definitions._
import service.persistence.modules.PersistenceModule
import scala.concurrent.duration._
import persistence.messages._
import akka.pattern.ask
import akka.util.{Timeout}
import persistence.PersistenceProxy

private[security] class ApiRequest[A](
  val companyName: String,
  val applicationName: String,
  request: Request[A]
) extends WrappedRequest[A](request)

private[security] case class ApiAction[A](action: Action[A]) extends Action[A] {
  
  private val persistenceProxy = PersistenceProxy.getInstance()

  private lazy val collection = "RedirectionTable"

  private def saveAppData(token: String, companyName: String, applicationName: String) = {
    val model = Json.obj(
      "token" -> token,
      "companyName" -> companyName,
      "applicationName" -> applicationName
    )
    persistenceProxy ! new Insert(null, collection, model)
  }

  private def getAppData(token: String): Future[Option[JsValue]] = {
    val query = new Get(null, collection, "token", token, null, true)
    implicit val timeout = Timeout(5 second)
    (persistenceProxy ? query).mapTo[PROptionResponse].map {res =>
      res.res
    }
  }

  private def deleteAppData(token: String) = {
    getAppData(token) map {res =>
      res match {
        case Some(data) => {
          persistenceProxy ! new Delete(null, collection, data, true)
        }
        case None => {}
      }
    }
  }


  private val TokenHeader = ApiSecurityAction.TokenHeader
  lazy val parser = action.parser

  def apply(request: Request[A]): Future[Result] = {
    request.headers.get(TokenHeader).orElse(None) match {
      case Some(token) => {
        this.getAppData(token.filter(_ != '"')) flatMap {res =>
          res match {
            case Some(json) => {
              val companyName = (json \ "companyName").as[String]
              val applicationName = (json \ "applicationName").as[String]
              action(new ApiRequest(companyName, applicationName, request))
            }
            case _ => {
              Logger.info("HTTP Request error: SDK token does not exist")
              Future.successful(Forbidden(Json.obj("error" -> "SDK token does not exist")))
            }
          }
        }
      }
      case None => {
        Logger.info("HTTP Request error: Token not found in header")
        Future.successful(Forbidden(Json.obj("error" -> "Token not found in header")))
      }
    }
  }
}

object ApiSecurityAction extends ActionBuilder[ApiRequest] {

  lazy val TokenHeader = "SDK-TOKEN"

  def invokeBlock[A](request: Request[A], block: (ApiRequest[A] => Future[Result])) = {
    request match {
      case req: ApiRequest[A] => block(req)
      case _ => Future.successful(BadRequest(Json.obj("error" -> "Invalid Request")))
    }
  }

  override def composeAction[A](action: Action[A]) = ApiAction(action)
}

