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
import java.util.Date
import org.bson.types.ObjectId
import play.api.libs.json.JsObject
import play.api.libs.json.JsValue
import models.application._

trait ApplicationMessageRequest extends WazzaMessage {
  def direct: Boolean
}

case class ARInsert(
  var sendersStack: Stack[ActorRef],
  companyName: String,
  application: WazzaApplication,
  direct: Boolean = false,
  hash: String = null
) extends ApplicationMessageRequest

case class ARAddPayPalCredentials(
  var sendersStack: Stack[ActorRef],
  companyName: String,
  applicationName: String,
  paypalCredentials: PayPalCredentials,
  direct: Boolean = false,
  hash: String = null
) extends ApplicationMessageRequest

case class ARAddPaymentSystem(
  var sendersStack: Stack[ActorRef],
  companyName: String,
  applicationName: String,
  paymentSystem: Int,
  direct: Boolean = false,
  hash: String = null
) extends ApplicationMessageRequest

case class ARDelete(
  var sendersStack: Stack[ActorRef],
  companyName: String,
  application: WazzaApplication,
  direct: Boolean = false,
  hash: String = null
) extends ApplicationMessageRequest

case class ARExists(
  var sendersStack: Stack[ActorRef],
  companyName: String,
  name: String,
  direct: Boolean = false,
  hash: String = null
) extends ApplicationMessageRequest

case class ARFind(
  var sendersStack: Stack[ActorRef],
  companyName: String,
  appName: String,
  direct: Boolean = false,
  hash: String = null
) extends ApplicationMessageRequest

