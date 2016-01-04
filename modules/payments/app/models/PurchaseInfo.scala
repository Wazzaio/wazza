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

package models.payments

import scala.language.implicitConversions
import play.api.libs.functional.syntax._
import play.api.libs.json._
import java.util.Date
import models.common._
import scala.util.{Try, Failure, Success}
import play.api.Logger

/**
  Purchase Id format: Hash(appName + itemID + time + device)
**/
abstract class PurchaseInfo {
  val id: String
  val sessionId: String
  val userId: String
  val itemId: String
  val price: Double
  val time: Date
  val deviceInfo: DeviceInfo
  val location: Option[LocationInfo]
  val paymentSystem: Int
  val success: Boolean

  def toJson(): JsValue

}

object PurchaseInfo {

  lazy val Id = "id"
  lazy val UserId = "userId"
  def getCollection(companyName: String, applicationName: String) = s"${companyName}_purchases_${applicationName}"

  def getLocation(json: JsValue) = {
    if(json.as[JsObject].keys.contains("location")) {
      (json \ "location").validate[LocationInfo].asOpt
    } else {
      None
    }
  }

  implicit def buildFromJson(json: JsValue): PurchaseInfo = {
    val possibleResult = (json \ "paymentSystem").as[Int] match {
      case PayPalPayment.Type => PayPalPayment.fromJson(json)
      case InAppPurchasePayment.Type => InAppPurchasePayment.fromJson(json)
    }

    possibleResult match {
      case Success(result) => result
      case Failure(e) => {
        Logger.error(s"Error building payment info from json: ${json}\n${e}")
        throw e
      }
    }
  }
}

