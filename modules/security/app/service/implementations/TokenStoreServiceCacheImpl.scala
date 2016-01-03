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

import play.api.cache.Cache
import play.api.Play._

class TokenStoreServiceCacheImpl extends TokenStoreService{

  def put(token: TokenId, userId: UserId, timeoutInSeconds: Int) = {
    Cache.set(token, userId, timeoutInSeconds)
  }

  def remove[T <: String](element: T): Unit = {
    Cache.remove(element)
  }

  def get(token: AuthenticityToken): Option[UserId] = {
    Cache.get(token).map(_.asInstanceOf[UserId])
  }
}
