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

package user.messages

import common.messages._
import akka.actor.{ActorRef}
import scala.collection.mutable.Stack
import play.api.libs.json._
import models.user._

trait UserResponse[T] extends WazzaMessage {
  val res: T
}

case class URUserResponse(
  var sendersStack: Stack[ActorRef],
  res: User,
  hash: String = null
) extends UserResponse[User]

case class UROptionResponse(
  var sendersStack: Stack[ActorRef],
  res: Option[User],
  hash: String = null
) extends UserResponse[Option[User]]

case class URApplicationsResponse(
  var sendersStack: Stack[ActorRef],
  res: List[String],
  hash: String = null
) extends UserResponse[List[String]]


case class URBooleanResponse(
  var sendersStack: Stack[ActorRef],
  res: Boolean,
  hash: String = null
) extends UserResponse[Boolean]

case class URValidationResponse(
  var sendersStack: Stack[ActorRef],
  res: Boolean,
  hash: String = null
) extends UserResponse[Boolean]

case class URAuthenticationResponse(
  var sendersStack: Stack[ActorRef],
  res: Option[User],
  hash: String = null
) extends UserResponse[Option[User]]

