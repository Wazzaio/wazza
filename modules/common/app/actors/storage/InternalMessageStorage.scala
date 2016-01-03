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

package common.actors

import akka.actor.{ActorRef}
import common.messages._
import java.security.SecureRandom
import java.math.BigInteger
import scala.collection.mutable.Map

class InternalMessageStorage[T <: WazzaMessage] {

  case class StorageElement(originalRequest: T, sender: ActorRef)

  private val BASE = 32
  private val BITS = 130
  private val random = new SecureRandom
  private def generateId = new BigInteger(BITS, random).toString(32)

  private val storage: Map[String, StorageElement] = Map()

  def store(sender: ActorRef, msg: T): String = {
    val id = generateId
    storage += (id -> new StorageElement(msg, sender))
    id
  }

  def get(id: String): Option[StorageElement] = {
    val s = storage.get(id)
    storage -= id
    s
  }
}
