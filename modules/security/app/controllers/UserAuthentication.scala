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
import service.security.modules.{SecurityModule}
import service.security.definitions.{TokenManagerService}
import scala.concurrent._
import ExecutionContext.Implicits.global

private[security] class UserRequest[A](val userId: String, request: Request[A]) extends WrappedRequest[A](request)

private[security] case class UserAction[A](action: Action[A]) extends Action[A] {

  private type AuthenticityToken = String

  lazy private val tokenService = Guice.createInjector(new SecurityModule)
    .getInstance(classOf[TokenManagerService])

  implicit val app: play.api.Application = play.api.Play.current
  private lazy val DefaultCacheExpiration = 7 * 24 * 3600 //7 days (in seconds)
  lazy val CacheExpiration = app.configuration.getInt("cache.expiration").getOrElse(DefaultCacheExpiration)

  private val AuthTokenHeader = UserAuthenticationAction.AuthTokenHeader//"X-XSRF-TOKEN"
  private val AuthTokenCookieKey = "XSRF-TOKEN"
  private val AuthTokenUrlKey = "auth"

  lazy val parser = action.parser

  def apply(request: Request[A]): Future[Result] = {
    val maybeToken = request.headers.get(AuthTokenHeader).orElse(request.getQueryString(AuthTokenUrlKey))
    maybeToken flatMap {token =>
      tokenService.get(token.filter(_ != '"'))
    } match {
      case Some(userId) => action(new UserRequest(userId, request))
      case _ => Future.successful(Forbidden("user not logged in"))
    }
  }
}

object UserAuthenticationAction extends ActionBuilder[UserRequest] {

  val AuthTokenHeader = "X-XSRF-TOKEN"
  def invokeBlock[A](request: Request[A], block: (UserRequest[A] => Future[Result])) = {
    request match {
      case req: UserRequest[A] => block(req)
      case _ => Future.successful(BadRequest("Invalid Request"))
    }
  }

  override def composeAction[A](action: Action[A]) = UserAction(action)
}

