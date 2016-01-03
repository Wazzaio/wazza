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

/**
  TODO - description
**/

case class MobileSessionInfo(
  hash: String,
  userId: String,
  applicationName: String,
  companyName: String
)

object MobileSessionInfo {

  def Id = "hash"
  def collection = "MobileSessionHashTable"

  implicit val readJson = (
    (__ \ "hash").read[String] and
    (__ \ "userId").read[String] and
    (__ \ "applicationName").read[String] and
    (__ \ "companyName").read[String]
  )(MobileSessionInfo.apply _)

  implicit val buildFromJson = (
    (__ \ "hash").write[String] and
    (__ \ "userId").write[String] and
    (__ \ "applicationName").write[String] and
    (__ \ "companyName").write[String]
  )(unlift(MobileSessionInfo.unapply))

}
