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

package models.common

import scala.language.implicitConversions
import play.api.libs.functional.syntax._
import play.api.Play.current
import play.api.libs.json._

case class DeviceInfo(
  osType: String,
  name: String,
  version: String,
  model: String
)

object DeviceInfo {

  implicit val readJson = (
    (__ \ "osType").read[String] and
      (__ \ "osName").read[String] and
      (__ \ "osVersion").read[String] and
      (__ \ "deviceModel").read[String]
  )(DeviceInfo.apply _)

  implicit val buildFromJson = (
    (__ \ "osType").write[String] and
      (__ \ "osName").write[String] and
      (__ \ "osVersion").write[String] and
      (__ \ "deviceModel").write[String]
  )(unlift(DeviceInfo.unapply))
}

