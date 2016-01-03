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

case class MobileSession(
  id: String, //hash
  userId: String,
  length: Double,
  startTime: Date,
  deviceInfo: DeviceInfo,
  purchases: List[String] //List of purchases id's
)

object MobileSession {

  val Id = "id"
  val Purchases = "purchases"

  def getCollection(companyName: String, applicationName: String) = s"${companyName}_mobileSessions_${applicationName}"

  implicit def buildFromJson(json: JsValue): MobileSession = {
    new MobileSession(
      (json \ "id").as[String],
      (json \ "userId").as[String],
      (json \ "length").as[Double],
      new Date((json \ "startTime").as[Long]),
      (json \ "device").validate[DeviceInfo].asOpt.get,
      (json \ "purchases").as[List[String]]
    )
  }

  implicit def toJson(session: MobileSession): JsValue = {
    Json.obj(
      "id" -> session.id,
      "userId" -> session.userId,
      "length" -> session.length,
      "startTime" -> session.startTime.getTime,
      "device" -> Json.toJson(session.deviceInfo),
      "purchases" -> session.purchases
    )
  }
}

