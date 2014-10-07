package service.user.definitions

import models.user.{PurchaseInfo}
import play.api.libs.json.JsValue
import scala.util.Try
import scala.concurrent._

trait PurchaseService {

  def create(json: JsValue): PurchaseInfo

  def save(companyName: String, applicationName: String, info: PurchaseInfo): Future[Unit]

  def get(companyName: String, applicationName: String, id: String): Future[Option[PurchaseInfo]]

  def getUserPurchases(companyName: String, applicationName: String, userId: String): Future[List[PurchaseInfo]]

  def exist(companyName: String, applicationName: String, id: String): Future[Boolean]

  def delete(companyName: String, applicationName: String, info: PurchaseInfo): Future[Unit]
}

