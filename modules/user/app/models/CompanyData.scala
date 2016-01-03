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

import play.api.Play.current
import play.api.libs.json._
import scala.language.implicitConversions
import play.api.libs.functional.syntax._

case class CompanyData(
  name: String,
  apps: List[String]
)

object CompanyData {

  val Key = "name"
  val Apps = "apps"
  val Collection = "companiesData"

  implicit val reader = (
    (__ \ "name").read[String] and
    (__ \ "apps").read[List[String]]
  )(CompanyData.apply _)

  implicit val write = (
    (__ \ "name").write[String] and
    (__ \ "apps").write[List[String]]
  )(unlift(CompanyData.unapply))
}
