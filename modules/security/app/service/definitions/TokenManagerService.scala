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

package service.security.definitions

import scala.util.Random
import java.security.SecureRandom
import java.time._

trait TokenManagerService {

  type AuthenticityToken = String
  type Id = String
  lazy val CookieName: String = "PLAY2AUTH_SESS_ID"
  protected val tokenSuffix = ":token"
  protected val userIdSuffix = ":userId"
  protected val random = new Random(new SecureRandom())

  private lazy val DefaultCacheExpiration = 7 * 24 * 3600 //7 days (in seconds)
  lazy val CacheExpiration = play.api.Play.current.configuration.getInt("cache.expiration").getOrElse(DefaultCacheExpiration)

  def startNewSession(userId: Id, timeoutInSeconds: Int = DefaultCacheExpiration): AuthenticityToken

  def remove(token: AuthenticityToken): Unit

  def get(token: AuthenticityToken): Option[String]

  def prolongTimeout(token: AuthenticityToken, timeoutInSeconds: Int): Unit
}
