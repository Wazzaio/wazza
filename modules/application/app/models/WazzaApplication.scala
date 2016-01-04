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
import scala.language.implicitConversions
import play.api.libs.functional.syntax._

case class Credentials(
  appId: String,
  sdkToken: String
)

object Credentials {
  implicit val reader = (
    (__ \ "appId").read[String] and
    (__ \ "sdkToken").read[String]
  )(Credentials.apply _)

  implicit val write = (
    (__ \ "appId").write[String] and
    (__ \ "sdkToken").write[String]
  )(unlift(Credentials.unapply))

  implicit def toJson(credential: Credentials): JsValue = {
    Json.obj(
      "appId" -> credential.appId,
      "sdkToken" -> credential.sdkToken
    )
  }

  implicit def fromJson(json: JsValue): Credentials = {
    new Credentials(
      (json \ "appId").as[String],
      (json \ "sdkToken").as[String]
    )
  }
}

case class PayPalCredentials(
  clientId: String,
  secret: String
)

object PayPalCredentials {
  implicit val reader = (
    (__ \ "clientId").read[String] and
    (__ \ "secret").read[String]
  )(PayPalCredentials.apply _)

  implicit val writer = (
    (__ \ "clientId").write[String] and
    (__ \ "secret").write[String]
  )(unlift(PayPalCredentials.unapply))
}

case class WazzaApplication(
  name: String,
  appUrl: String,
  var imageName: String,
  packageName: String,
  appType: List[String], /** Platforms **/
  credentials: Credentials,
  paypalCredentials: Option[PayPalCredentials],
  paymentSystems: List[Int],
  items: List[Item] = List[Item](),
  virtualCurrencies: List[VirtualCurrency] = List[VirtualCurrency]()
)

object WazzaApplication {

  def getCollection(companyName: String, applicationName: String) = s"${companyName}_apps_${applicationName}"

  lazy val Key = "name"
  lazy val ItemsId = "items"
  lazy val VirtualCurrenciesId = "virtualCurrencies"
  lazy val CredentialsId = "credentials"
  lazy val applicationTypes = List("iOS", "Android")
}

package object WazzaApplicationImplicits {

  implicit def buildFromJson(json: JsValue): WazzaApplication = {
    new WazzaApplication(
      (json \ "name").as[String],
      (json \ "appUrl").as[String],
      (json \ "imageName").as[String],
      (json \ "packageName").as[String],
      (json \ "appType").as[List[String]],
      (json \ "credentials").validate[Credentials].get,
      ((json \ "payPalCredentials").as[Option[JsValue]] match {
        case Some(credentials) => credentials.validate[PayPalCredentials].fold(valid = {i => Some(i)}, invalid = {_ => None})
        case None => None
      }),
      (json \ "paymentSystems").as[List[Int]],
      (json \ "items").as[JsArray],
      (json \ "virtualCurrencies").as[JsArray]
    )
  }

  implicit def buildOptionFromOptionJson(json: Option[JsValue]): Option[WazzaApplication] = {
    json match {
      case Some(app) => Some(buildFromJson(app))
      case None => None
    }
  }

  implicit def convertToJson(application: WazzaApplication): JsValue = {
    val json = Json.obj(
      "name" -> application.name,
      "appUrl" -> application.appUrl,
      "imageName" -> application.imageName,
      "packageName" -> application.packageName,
      "appType" -> application.appType,
      "credentials" -> Json.toJson(application.credentials),
      "paymentSystems" -> Json.toJson(application.paymentSystems),
      "items" -> JsArray(application.items.map{item =>
        Item.convertToJson(item)
      }.toSeq),
      "virtualCurrencies" -> JsArray(application.virtualCurrencies.map {vc =>
        VirtualCurrency.buildJson(vc)
      })
    )
    
    application.paypalCredentials match {
      case Some(credentials) => {
        json ++ Json.obj("payPalCredentials" -> Json.toJson(credentials))
      }
      case None => json
    }
  }

  implicit def buildOptionCredentialsFromJson(json: Option[JsValue]): Option[Credentials] = {
    json match {
      case Some(j) => {
          (j \ WazzaApplication.CredentialsId).validate[Credentials].fold(
          valid = {c => Some(c)},
          invalid = {_ => None}
        )
      }
      case None => None
    }
  }
}

