package models.application

import java.text.SimpleDateFormat
import java.util.Date
import play.api.Play.current
import play.api.libs.json._
import scala.language.implicitConversions
import play.api.libs.functional.syntax._

trait InAppPurchaseMetadata {
  def osType: String
  def itemId: String
  def title: String
  def description: String
}

case class GoogleTranslations(
  locale: String,
  title: String,
  description: String
)

object GoogleTranslations {
  implicit val format = Json.format[GoogleTranslations]
}

case class GoogleMetadata(
  override val osType: String,
  override val itemId: String,
  override val title: String,
  override val description: String,
  publishedState: String,
  purchaseType: String,
  autoTranslate: Boolean,
  locale: List[GoogleTranslations],
  autofill: Boolean,
  language: String,
  price: Double,
  countries: List[String]

) extends InAppPurchaseMetadata

object GoogleMetadata {

  implicit val format = Json.format[GoogleMetadata]
}

case class AppleMetadata(
  override val osType: String,
  override val itemId: String,
  override val title: String,
  override val description: String,
  productProperties: AppleProductProperties,
  languageProperties: AppleLanguageProperties,
  pricingProperties: ApplePricingProperties

) extends InAppPurchaseMetadata

case class AppleProductProperties(
  productType: String, 
  status: String,
  reviewNotes: String
  // screenshot: TODO
)

case class AppleLanguageProperties(
  language: String,
  //display: String,
  description: String
  //publicationName: String
)

case class ApplePricingProperties(
  clearedForSale: String,
  price: Double,
  pricingAvailability: PricingAvailability
)

object ApplePricingProperties {
  lazy val ClearedForSale = "Yes"
  lazy val NotClearedForSale = "No"
}

case class PricingAvailability(
  begin: Date,
  end: Date
)

//Not used at the moment
case class AppleDurationProperties(
  autoRenewalDuration: Date,
  freeTrialDuration: Date,
  marketingIncentiveDuration: Date
)

object InAppPurchaseMetadata {

  lazy val Android = "android"
  lazy val IOS = "iOS"

  lazy val ConsumableProduct = "Consumable"
  lazy val NonConsumableProduct = "Non-Consumable"
  lazy val NonRenewSubscription = "Non-renewing subscription"
  lazy val DateFormat = "MM/dd/yyyy HH:mm:ss"

  val LanguageCodes = Map(
    "Chinese" ->    "zh_TW",
    "Italian" ->    "it_IT",
    "Czech" ->      "cs_CZ",
    "Japanese" ->   "ja_JP",
    "Danish" ->     "da_DK",
    "Korean" ->     "ko_KR",
    "Dutch" ->      "nl_NL",
    "Norwegian" ->  "no_NO",
    "English" ->    "en_US",
    "Polish" ->     "pl_PL",
    "French" ->     "fr_FR",
    "Portuguese" -> "pt_PT",
    "Finnish" ->    "fi_FI",
    "Russian" ->    "ru_RU",
    "German" ->     "de_DE",
    "Spanish" ->    "es_ES",
    "Hebrew" ->     "iw_IL",
    "Swedish" ->    "sv_SE",
    "Hindi" ->      "hi_IN"
  )

  implicit def buildJson(metadata: InAppPurchaseMetadata): JsValue = {
    metadata match {
      case google: GoogleMetadata => {
        Json.obj(
          "osType" -> google.osType,
          "itemId" -> google.itemId,
          "title" -> google.title,
          "description" -> google.description,
          "publishedState" -> google.publishedState,
          "purchaseType" -> google.purchaseType,
          "autoTranslate" -> google.autoTranslate,
          "locale" -> Json.toJson(google.locale.map((el: GoogleTranslations) => {
            Json.obj("locale" -> el.locale, "title" -> el.title, "description" -> el.description)
          })),
          "autofill" -> google.autofill,
          "language" -> google.language,
          "price" -> google.price,
          "countries" -> google.countries
        )
      }
      case apple: AppleMetadata => {
        val df = new SimpleDateFormat(DateFormat)
        Json.obj(
          "osType" -> apple.osType,
          "itemId" -> apple.itemId,
          "title" -> apple.title,
          "description" -> apple.description,
          "productProperties" -> Json.obj(
            "type" -> apple.productProperties.productType,
            "status" -> apple.productProperties.status,
            "reviewNotes" -> apple.productProperties.reviewNotes
          ),
          "languageProperties" -> Json.obj(
            "language" -> apple.languageProperties.language,
            "description" -> apple.description
          ),
          "pricingProperties" -> Json.obj(
            "clearedForSale" -> apple.pricingProperties.clearedForSale,
            "price" -> apple.pricingProperties.price,
            "pricingAvailability" -> Json.obj(
              "begin" -> df.format(apple.pricingProperties.pricingAvailability.begin),
              "end" -> df.format(apple.pricingProperties.pricingAvailability.end)
            )
          )
        )
      }
      case _ => null
    }
  }

  private implicit def buildLocateFromJson(array: JsArray): List[GoogleTranslations] = {
    array.value.map{(el: JsValue) =>
      new GoogleTranslations(
        (el \ "locale").as[String],
        (el \ "title").as[String],
        (el \ "description").as[String]
      )
    }.toList
  }

  implicit def buildFromJson(json: JsValue): InAppPurchaseMetadata = {
    val metadataType = (json \ "osType").as[String]
    metadataType match {
      case Android => {
        new GoogleMetadata(
          (json \ "osType").as[String],
          (json \ "itemId").as[String],
          (json \ "title").as[String],
          (json \ "description").as[String],
          (json \ "publishedState").as[String],
          (json \ "purchaseType").as[String],
          (json \ "autoTranslate").as[Boolean],
          (json \ "locale").as[JsArray],
          (json \ "autofill").as[Boolean],
          (json \ "language").as[String],
          (json \ "price").as[Double],
          (json \ "countries").as[List[String]]
        )
      }
      case IOS => {
        val df = new SimpleDateFormat(DateFormat)
        new AppleMetadata(
          (json \ "osType").as[String],
          (json \ "itemId").as[String],
          (json \ "title").as[String],
          (json \ "description").as[String],
          new AppleProductProperties(
            (json \ "productProperties" \ "type").as[String],
            (json \ "productProperties" \ "status").as[String],
            (json \ "productProperties" \ "reviewNotes").as[String]
          ),
          new AppleLanguageProperties(
            (json \ "languageProperties" \ "language").as[String],
            (json \ "languageProperties" \ "description").as[String]
          ),
          new ApplePricingProperties(
            (json \ "pricingProperties" \ "clearedForSale").as[String],
            (json \ "pricingProperties" \ "price").as[Double],
            new PricingAvailability(
              df.parse((json \ "pricingProperties" \ "pricingAvailability" \ "begin").as[String]),
              df.parse((json \ "pricingProperties" \ "pricingAvailability" \ "end").as[String])
            )
          )
        )
      }
    }
  }
}

package object InAppPurchaseContext {

  import java.util.Date

  // Stores Info
  lazy val GoogleStoreId = 0
  lazy val AppleStoreId = 1
  lazy val GoogleMetadataType = "models.application.GoogleMetadata"
  lazy val AppleMetadataType = "models.application.AppleMetadata"

  // Currency Types
  lazy val VirtualCurrencyType = 0
  lazy val RealWordCurrencyType = 1

  // Purchase Types
  lazy val ManagedProduct = 0
  lazy val Subscription = 1
  lazy val UnManaged = 2

}
