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

package controllers.user

import play.api._
import play.api.mvc._
import play.api.data.Forms._
import play.api.data._
import models.user._
import com.mongodb.casbah.Imports._
import service.user.definitions._
import com.google.inject._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import play.api.libs.json._
import service.security.definitions.{TokenManagerService}
import controllers.security._
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration._
import controllers.security.{UserAuthenticationAction}
import user.{UserProxy}
import akka.util.{Timeout}
import akka.pattern.ask
import scala.collection.mutable.Stack
import user.messages._

class RegistrationController @Inject()(
  tokenService: TokenManagerService
) extends Controller with CookieManager {

  private val userProxy = UserProxy.getInstance()
  implicit val timeout = Timeout(5 second)

  private def validateUser(email: String): Boolean = {
    import user.{UserProxy}
    import user.messages._
    import scala.collection.mutable.Stack
    import akka.util.{Timeout}
    import akka.pattern.ask
    import user.messages._

    val request = new URValidate(new Stack, email, true)
    implicit val timeout = Timeout(10 seconds)
    val futureValidation = userProxy ? request
    Await.result(futureValidation, timeout.duration).asInstanceOf[URValidationResponse].res
  }

  val registrationForm : Form[User] = Form(
    mapping(
      "name" -> nonEmptyText,
      "email" -> (nonEmptyText verifying email.constraints.head),
      "password" -> nonEmptyText,
      "company" -> nonEmptyText,
      "applications" -> ignored(List[String]())
    )(User.apply)(User.unapply) verifying("User with this email already exists", fields => fields match {
      case userData => validateUser(userData.email)
    })
  )

  def registerUser = Action {
    Ok(views.html.registerUser())
  }

  def submitUser = Action.async(parse.json) { implicit request =>
    registrationForm.bindFromRequest.fold(
      formErrors => Future {
        BadRequest(Json.obj("errors" -> formErrors.errors.head.message))
      },
      user => {
        val token = tokenService.startNewSession(user.email)
        userProxy ! new URInsert(new Stack, user, true)
        Future.successful(
          Ok(Json.obj(
            "authToken" -> token,
            "userId" -> user.email,
            "url" -> "analytics.overview"
          )).withToken(token)
        )
      }
    )
  }
}
