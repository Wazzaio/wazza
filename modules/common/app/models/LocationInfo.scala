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
import play.api.libs.json._

case class LocationInfo(
  latitude: Double,
  longitude: Double
)

object LocationInfo {
  implicit val reader  = (
    (__ \ "latitude").read[Double] and
      (__ \ "longitude").read[Double]
  )(LocationInfo.apply _)

  implicit val write = (
    (__ \ "latitude").write[Double] and
      (__ \ "longitude").write[Double]
  )(unlift(LocationInfo.unapply))
}
