package service.application.implementations

import com.github.nscala_time.time.Imports._
import java.text.SimpleDateFormat
import org.joda.time.Days
import java.util.Date
import service.application.definitions.ItemService
import service.application.definitions.ApplicationService
import models.application._
import com.google.inject._
import scala.util.{Try, Success, Failure}
import InAppPurchaseContext._
import play.api.mvc.{MultipartFormData}
import play.api.libs.json._
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import com.github.nscala_time.time.Imports._
import play.api.libs.Files._
import java.io.File
import java.io.PrintWriter
import play.api.mvc.MultipartFormData._
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.language.implicitConversions
import service.aws.definitions.PhotosService

class ItemServiceImpl @Inject()(
  applicationService: ApplicationService,
  photosService: PhotosService
) extends ItemService {
  
  private lazy val MultiplyDelta = 1000000

  def createGooglePlayItem(
    applicationName: String,
    name: String,
    description: String,
    typeOfCurrency: Int,
    virtualCurrency: Option[String],
    price: Double,
    publishedState: String,
    purchaseType: String,
    autoTranslate: Boolean,
    autofill: Boolean,
    language: String,
    imageName: String,
    imageUrl: String
  ): Future[Try[Item]] = {

    val promise = Promise[Try[Item]]

    val metadata = new GoogleMetadata(
      InAppPurchaseMetadata.Android,
      generateId(name, purchaseType),
      name,
      description,
      publishedState,
      purchaseType,
      autoTranslate,
      List[GoogleTranslations](GoogleTranslations(InAppPurchaseMetadata.LanguageCodes.get(language).get, name, description)),
      autofill,
      language,
      price,
      applicationService.getApplicationCountries(applicationName)
    )

    val item = new Item(
      name,
      description,
      GoogleStoreId,
      metadata,
      new Currency(typeOfCurrency, price, virtualCurrency),
      imageInfo = new ImageInfo(imageName, imageUrl)
    )

    promise.success(applicationService.addItem(item, applicationName))
    promise.future
  }

  def createAppleItem(
    applicationName: String,
    title: String,
    description: String,
    price: Double,
    imageName: String, //TODO: change image name to IAP name
    imageUrl: String
  ): Future[Try[Item]] = {


    def renameItemImage(newName: String): Future[String] = {
      photosService.copyAndDelete(imageName, newName) map {res =>
        res.s3Url
      }
    }

    val promise = Promise[Try[Item]]

    val now = DateTime.now
    val end = now + 1.years
    val itemId = generateId(title,InAppPurchaseMetadata.ConsumableProduct)

    val metadata = new AppleMetadata(
      InAppPurchaseMetadata.IOS,
      itemId,
      title,
      description,
      new AppleProductProperties(
        InAppPurchaseMetadata.ConsumableProduct,
        "", //status
        "" //review notes
      ),
      new AppleLanguageProperties(
        "English",
        description
      ),
      new ApplePricingProperties(
        ApplePricingProperties.ClearedForSale,
        price,
        new PricingAvailability(now.toDate, end.toDate)
      ) 
    )

    renameItemImage(itemId) map {url =>
      val item = new Item(
        title,
        description,
        AppleStoreId,
        metadata,
        new Currency(RealWordCurrencyType, price, None),
        new ImageInfo(itemId, url)
      )
      promise.success(applicationService.addItem(item, applicationName))
    } recover {
      case err: Exception => promise.failure(err)
    }
    
    promise.future
  }

  def createItemFromMultipartData(formData: MultipartFormData[_], applicationName: String): Future[Try[Item]] = {
    def generateJsonError(key: String, message: String): JsValue = {
      Json.obj(key -> Json.obj("message" -> message, "visible" -> true))
    }

    var errors = ListBuffer[JsValue]()
    val data = formData.dataParts
    var itemData = Map[String,JsValue]()

    data get "name" match {
      case Some(name) => {
        if(applicationService.itemExists(name.head, "application name")){
          errors += generateJsonError("name", "An item already exists with this name")
        }
      }
      case None => errors += generateJsonError("name", "Please insert a name")
    }

    data get "metadata" match {
      case Some(m) => itemData += ("metadata" -> Json.parse(m.head))
      case None => errors += generateJsonError("metadata", "Missing metadata information")
    }

    data get "currency" match {
      case Some(currency) => {
        val value = (Json.parse(currency.head) \ "value").as[Double]
        if(value <= 0.0){
          errors += generateJsonError("currency", "Price must be greater than zero")
        } else {
          itemData += ("currency" -> Json.parse(currency.head))
        }
      }
      case None => errors += generateJsonError("currency", "Missing currency information")
    }

    data get "imageInfo" match {
      case Some(info) => itemData += ("imageInfo" -> Json.parse(info.head))
      case None => errors += generateJsonError("imageInfo", "No image info")
    }
    
    if(errors.size > 0){
      val promise = Promise[Try[Item]]
      promise.failure(new Exception(JsArray(errors).toString))
      promise.future
    } else {
      val osType = (itemData("metadata") \ "osType").as[String]
      osType match {
        case InAppPurchaseMetadata.Android => {
          createGooglePlayItem(
            applicationName,
            (data get "name").get.head,
            (data get "description").get.head,
            (getCurrencyTypes get (itemData("currency") \ "typeOf").as[String]).get,
            (itemData("currency") \ "virtualCurrency").asOpt[String],
            (itemData("currency") \ "value").as[Double],
            (itemData("metadata") \ "publishedState").as[String],
            (itemData("metadata") \ "purchaseType").as[String],
            (itemData("metadata") \ "autoTranslate").as[Boolean],
            (itemData("metadata") \ "autofill").as[Boolean],
            (itemData("metadata") \ "language").as[String],
            (itemData("imageInfo") \ "name").as[String],
            (itemData("imageInfo") \ "url").as[String]
          )
        }
        case InAppPurchaseMetadata.IOS => {
          createAppleItem(
            applicationName,
            (data get "name").get.head,
            (data get "description").get.head,
            (itemData("currency") \ "value").as[Double],
            (itemData("imageInfo") \ "name").as[String],
            (itemData("imageInfo") \ "url").as[String]
          )
        }
      }
    }
  }

  def getCurrencyTypes(): Map[String, Int] = {
    Map("Real" -> RealWordCurrencyType, "Virtual" -> VirtualCurrencyType)
  }

  def generateMetadataFile(item: Item): File = {
    def escape(str: String): String = {
      str.replaceAll(";","\\;").replaceAll("\"", "\\")
    }

    def parseContent(content: String): String = {
      lazy val DescriptionSectionThreshold = 2
      lazy val MainSeparator = ","
      lazy val SubsectionSeparator = ';'
      val parsedContent = content.replace("\n","").split(MainSeparator)
      val result = ArrayBuffer[String]()
      parsedContent.map({(el: String) =>
        if(el.count(_ == SubsectionSeparator) > DescriptionSectionThreshold){
          result += s"${el.dropRight(1)},"
        } else {
          result += s"$el,"
        }
      })
      result.mkString.dropRight(1)
    }

    def dateConverter(date: Date): String = {
      val formater = new SimpleDateFormat("yyyy-MM-dd")
      formater.format(date)
    }

    item.metadata match {
      case google: GoogleMetadata => {
        val file = new File(s"/tmp/" + item.name + ".csv")
        val writer = new PrintWriter(file)
        val content = views.txt.csv.csvGoogleTemplate.render(
          google.itemId,
          google.publishedState,
          google.purchaseType,
          google.autoTranslate,
          google.locale,
          escape(google.title),
          escape(google.description),
          google.autofill,
          List("PT"), //TODO default
          (google.price * MultiplyDelta).toInt
        ).body

        writer.write(parseContent(content))
        writer.close()
        file
      }
      case apple: AppleMetadata => {
        val file = new File(s"/tmp/" + apple.itemId + ".txt")
        val writer = new PrintWriter(file)
        val content = views.txt.csv.appleIAPTemplate.render(
            "sku: String",
            apple.itemId,
            apple.title,
            apple.productProperties.productType,
            apple.pricingProperties.clearedForSale,
            apple.pricingProperties.price,
            apple.title,
            apple.languageProperties.description,
            item.imageInfo.url,
            dateConverter(apple.pricingProperties.pricingAvailability.begin),
            dateConverter(apple.pricingProperties.pricingAvailability.begin)
            
        ).body
        writer.write(content)
        writer.close()
        file
      }
      case _ => null
    }
  }

  protected def generateId(name: String, purchaseType: String): String = {
    val date = DateTime.now.toString()
    s"${date.replace(":","_").replace("-","_").toLowerCase}.${name.toLowerCase.replace(" ", "_")}.$purchaseType"
  }

  private implicit def extractFile(filePart: FilePart[_]): File = {
    filePart.ref match {
      case TemporaryFile(file) => file
    }
  }
}

