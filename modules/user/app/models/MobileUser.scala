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

package models.user

import scala.language.implicitConversions
import play.api.libs.functional.syntax._
import play.api.Play.current
import play.api.libs.json._
import java.util.Date
import models.common._

case class SessionResume(id: String, startTime: Date, platform: String)
case class PurchaseResume(id: String, time: Date, platform: String, paymentSystem: Int)
case class MobileUser(
  userId: String,
  sessions: List[SessionResume],
  purchases: List[PurchaseResume],
  devices: List[DeviceInfo]
)

object MobileUser {

  lazy val KeyId = "userId"
  lazy val SessionsKey = "sessions"
  lazy val PurchasesKey = "purchases"

  def getCollection(companyName: String, applicationName: String) = s"${companyName}_mUsers_${applicationName}"

  implicit def readJson(mobileUser: MobileUser): JsValue = {
    Json.obj(
      "userId" -> mobileUser.userId,
      "sessions" -> mobileUser.sessions.map(readJsonSessionResume(_)),
      "purchases" -> mobileUser.purchases.map(readJsonPurchaseResume(_)),
      "devices" -> mobileUser.devices.map(Json.toJson(_))
    )
  }

  implicit def buildFromJson(json: JsValue): MobileUser = {
    new MobileUser(
      (json \ "userId").as[String],
      (json \ "sessions").as[JsArray].value.toList.map(buildSessionResumeFromJson(_)),
      (json \ "purchases").as[JsArray].value.toList.map(buildPurchaseResumeFromJson(_)),
      (json \ "devices").as[JsArray].value.toList.map(_.validate[DeviceInfo].asOpt.get)
    )
  }

  implicit def readJsonSessionResume(sessionResume: SessionResume): JsValue = {
    Json.obj(
      "id" -> sessionResume.id,
      "startTime" -> sessionResume.startTime.getTime,
      "platform" -> sessionResume.platform
    )
  }

  implicit def buildSessionResumeFromJson(json: JsValue): SessionResume = {
    new SessionResume(
      (json \ "id").as[String],
      new Date((json \ "startTime").as[Long]),
      (json \ "platform").as[String]
    )
  }

  implicit def readJsonPurchaseResume(purchaseResume: PurchaseResume): JsValue = {
    Json.obj(
      "id" -> purchaseResume.id,
      "time" -> purchaseResume.time.getTime,
      "platform" -> purchaseResume.platform,
      "paymentSystem" -> purchaseResume.paymentSystem
    )
  }

  implicit def buildPurchaseResumeFromJson(json: JsValue): PurchaseResume = {
    new PurchaseResume(
      (json \ "id").as[String],
      new Date((json \ "time").as[Long]),
      (json \ "platform").as[String],
      (json \ "paymentSystem").as[Int]
    )
  }
}

