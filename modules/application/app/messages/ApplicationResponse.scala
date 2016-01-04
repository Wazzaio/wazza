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

package application.messages

import common.messages._
import akka.actor.{ActorRef}
import scala.collection.mutable.Stack
import play.api.libs.json._
import models.application._

trait ApplicationResponse[T] extends WazzaMessage {
  val res: T
}

case class ARWApplicationResponse(
  var sendersStack: Stack[ActorRef],
  res: WazzaApplication,
  hash: String = null
) extends ApplicationResponse[WazzaApplication]

case class ARBooleanResponse(
  var sendersStack: Stack[ActorRef],
  res: Boolean,
  hash: String = null
) extends ApplicationResponse[Boolean]

case class AROptionResponse(
  var sendersStack: Stack[ActorRef],
  res: Option[WazzaApplication],
  hash: String = null
) extends ApplicationResponse[Option[WazzaApplication]]

