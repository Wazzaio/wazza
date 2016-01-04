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

package controllers

import play.api._
import play.api.mvc._
import play.api.data.Forms._
import play.api.data._
import com.google.inject._
import service.user.definitions.{UserService}
import scala.concurrent._
import ExecutionContext.Implicits.global
import play.api.libs.json._
import models.user._
import play.api.libs.iteratee._
import scala.util.{Success, Failure}
import controllers.security._
import service.security.definitions.{TokenManagerService}
import scala.concurrent.duration._

class LoginLogoutController @Inject()(
  userService: UserService,
  tokenService: TokenManagerService
) extends Controller with CookieManager {

  val loginForm = Form {
    mapping(
      "email" -> email,
      "password" -> text
    )(userService.authenticate)(_.map(u => (u.email, "")))
      .verifying("Invalid email or password", result => result.isDefined)
  }

  def login = Action {
    Ok(views.html.login())
  }

  def logout = Action{implicit request =>
    request.headers.get(UserAuthenticationAction.AuthTokenHeader) map { token =>
      Ok(routes.Application.index().url).discardingToken(token)(tokenService.remove)
    } getOrElse BadRequest(Json.obj("err" -> "No Token"))
  }

  def authenticate = Action.async(parse.json){implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future {
          BadRequest(Json.obj("errors" -> formWithErrors.errors.head.message))
      },
      user => {
        val token = tokenService.startNewSession(user.get.email)
        Future {
          Ok(Json.obj(
            "companyName" -> user.get.company,
            "authToken" -> token,
            "userId" -> user.get.email,
            "userName" -> user.get.name,
            "url" -> "analytics.overview"
          )).withToken(token)
        }
      }
    )
  }
}
