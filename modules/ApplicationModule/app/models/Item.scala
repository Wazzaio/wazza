package models.application

import play.api.Play.current
import play.api.libs.json._
import java.util.Date
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import ApplicationMongoContext._

case class Item(
	id: String,
	name: String,
	description: String,
	store: Int,
	@Salat metadata: ItemMetadata,
	currency: Currency,
	purchaseInfo: PurchaseInfo = new PurchaseInfo
)

case class Currency(
	typeOf: Int, //virtual or real money
	value: Double
)

case class PurchaseInfo()

trait ItemMetadata {
	def osType: String
	def id: String
	def title: String
	def description: String
}

case class GoogleMetadata(
	override val id: String,
	override val title: String,
	override val description: String,
	publishedState: String,
	purchaseType: Int,
	autoTranslate: Boolean,
	locate: List[GoogleTranslations],
	autofill: Boolean,
	language: String,
	price: Double

)(implicit val osType: String = "Google") extends ItemMetadata

case class GoogleTranslations(
	locale: String,
	title: String,
	description: String
)

case class AppleMetadata(
	override val id: String,
	override val title: String,
	override val description: String,
	productProperties: AppleProductProperties,
	languageProperties: AppleLanguageProperties,
	pricingProperties: ApplePricingProperties,
	durationProperties: AppleDurationProperties

)(implicit val osType: String = "Apple") extends ItemMetadata

case class AppleProductProperties(
	productType: Int, 
	status: String,
	reviewNotes: String
	// screenshot: TODO
)

case class AppleLanguageProperties(
	language: String,
	display: String,
	description: String,
	publicationName: String
)

case class ApplePricingProperties(
	clearedForSale: Boolean,
	price: Double,
	pricingAvailability: PricingAvailability
)

case class PricingAvailability(
	begin: Date,
	end: Date
)

case class AppleDurationProperties(
	autoRenewalDuration: Date,
	freeTrialDuration: Date,
	marketingIncentiveDuration: Date
)

object Item extends ModelCompanion[Item, ObjectId] {

	val dao = new SalatDAO[Item, ObjectId](mongoCollection("applications")){}

	def getDAO = dao
}