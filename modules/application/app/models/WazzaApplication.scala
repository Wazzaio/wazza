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

case class WazzaApplication(
  name: String,
  appUrl: String,
  var imageName: String,
  packageName: String,
  appType: List[String],
  credentials: Credentials,
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
    Json.obj(
      "name" -> application.name,
      "appUrl" -> application.appUrl,
      "imageName" -> application.imageName,
      "packageName" -> application.packageName,
      "appType" -> application.appType,
      "credentials" -> Json.toJson(application.credentials),
      "items" -> JsArray(application.items.map{item =>
        Item.convertToJson(item)
      }.toSeq),
      "virtualCurrencies" -> JsArray(application.virtualCurrencies.map {vc =>
        VirtualCurrency.buildJson(vc)
      })
    )
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
