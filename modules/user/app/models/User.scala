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

case class User(
  name: String,
  email: String,
  var password: String,
  company: String,
  applications: List[String]
)

object User {

  implicit def buildFromOption(opt: Option[JsValue]): Option[User] = {
    opt match {
      case Some(jsonUser) => {
        jsonUser.validate[User].fold(
          valid = {v => Some(v)},
          invalid = {_ => None}
        )
      }
      case None => None
    }
  }

  val Id = "email"
  val ApplicationsField = "applications"
  def getCollection() = "company_user"

  implicit val userReadJson = (
    (__ \ "name").read[String] and
    (__ \ "email").read[String] and
    (__ \ "password").read[String] and
    (__ \ "company").read[String] and
    (__ \ "applications").read[List[String]]
  )(User.apply _)

  implicit val userBuildFromJson = (
    (__ \ "name").write[String] and
    (__ \ "email").write[String] and
    (__ \ "password").write[String] and
    (__ \ "company").write[String] and
    (__ \ "applications").write[List[String]]
  )(unlift(User.unapply))
}

