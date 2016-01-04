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

package models.application

import play.api.Play.current
import play.api.libs.json._
import java.util.Date
import scala.language.implicitConversions
import play.api.libs.functional.syntax._

case class VirtualCurrency(
  name: String,
  price: Double,
  inAppPurchaseMetadata: InAppPurchaseMetadata
)

object VirtualCurrency {

  val Id = "name"

  def buildJson(vc: VirtualCurrency): JsValue = {
    Json.obj(
      "name" -> vc.name,
      "price" -> vc.price,
      "inAppPurchaseMetadata" -> InAppPurchaseMetadata.buildJson(vc.inAppPurchaseMetadata),
      "elementId" -> "name",
      "attributeName" -> "virtualCurrencies"
    )
  }

  implicit def buildFromJson(json: Option[JsValue]): Option[VirtualCurrency] = {
    json match {
      case Some(vc) => {
        Some(new VirtualCurrency(
          (vc \ "name").as[String],
          (vc \ "price").as[Double],
          (vc \ "inAppPurchaseMetadata")
        ))
      }
      case None => None
    }
  }

  implicit def buildVCListFromJsonArray(array: JsArray): List[VirtualCurrency] = {
   array.value .map((json: JsValue) => {
      new VirtualCurrency(
        (json \ "name").as[String],
        (json \ "price").as[Double],
        (json \ "inAppPurchaseMetadata")
      )
    }).toList
  }
}
