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

package service.security.implementations

import scala.annotation.tailrec
import service.security.definitions._
import com.google.inject._

class TokenManagerServiceImpl @Inject()(
  tokenStoreService: TokenStoreService
) extends TokenManagerService{

  def startNewSession(userId: Id, timeoutInSeconds: Int): AuthenticityToken = {
    removeByUserId(userId)
    val token = generate
    store(token, userId, timeoutInSeconds)
    token
  }

  def remove(token: AuthenticityToken): Unit = {
    get(token) foreach unsetUserId
    unsetToken(token)
  }

  def get(token: AuthenticityToken): Option[String] = {
    tokenStoreService.get(token + tokenSuffix)
  }

  def prolongTimeout(token: AuthenticityToken, timeoutInSeconds: Int): Unit = {
    get(token).foreach(store(token, _, timeoutInSeconds))
  }

  private def unsetToken(token: AuthenticityToken) {
    tokenStoreService.remove(token + tokenSuffix)
  }

  private def unsetUserId(userId: Id) {
    tokenStoreService.remove(userId.toString + userIdSuffix)
  }

  private def store(token: AuthenticityToken, userId: Id, timeoutInSeconds: Int) {
    tokenStoreService.put(token + tokenSuffix, userId, timeoutInSeconds)
    tokenStoreService.put(userId.toString + userIdSuffix, token, timeoutInSeconds)
  }

  @tailrec
  private final def generate: AuthenticityToken = {
    val table = "abcdefghijklmnopqrstuvwxyz1234567890_.!~*'()"
    val token = Stream.continually(random.nextInt(table.size)).map(table).take(64).mkString
    if (get(token).isDefined) generate else token
  }

  private def removeByUserId(userId: Id) {
    tokenStoreService.get(userId.toString + userIdSuffix) foreach unsetToken
    unsetUserId(userId)
  }
}
