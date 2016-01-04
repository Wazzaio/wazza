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
import java.util.Date
import play.api.libs.json.JsObject
import play.api.libs.json.JsValue
import models.user._
import models.common._

abstract class MobileUserMessageRequest extends WazzaMessage {
  val direct: Boolean 
  val companyName: String
  val applicationName: String
  val userId: String
} 

case class MUCreate(
  direct: Boolean,
  companyName: String,
  applicationName: String,
  userId: String,
  deviceInfo: DeviceInfo,
  var sendersStack: Stack[ActorRef],
  hash: String = null
) extends MobileUserMessageRequest
 
case class MUAddSessionInfo(
  direct: Boolean,
  companyName: String,
  applicationName: String,
  userId: String,
  var sendersStack: Stack[ActorRef],
  sessionId: String,
  sessionStart: Date,
  platform: String,
  hash: String = null
) extends MobileUserMessageRequest

case class MUAddPurchaseId(
  direct: Boolean,
  companyName: String,
  applicationName: String,
  userId: String,
  var sendersStack: Stack[ActorRef],
  purchaseId: String,
  purchaseDate: Date,
  platform: String,
  paymentSystem: Int,
  hash: String = null
) extends MobileUserMessageRequest

