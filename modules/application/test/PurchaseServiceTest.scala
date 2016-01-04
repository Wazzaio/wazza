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

package test.application
/**
import models.user.DeviceInfo
import models.user.DeviceInfo
import models.user.LocationInfo
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import scala.util.Failure
import scala.util.Success
import com.google.inject._
import service.persistence.implementations.{MongoDatabaseService}
import service.user.implementations.MobileUserServiceImpl
import service.user.implementations.PurchaseServiceImpl
import models.user.PurchaseInfo

class PurchaseServiceTest extends Specification {

  private object PurchaseData {
    val id = "id"
    val userId = "user"
    val applicationName = "app name"
    val itemId = "itemId"
    val price = 1.99
    val time = "today"
    val location = new LocationInfo(1,2)
    val deviceInfo = new DeviceInfo("iOS", "iPhone", "5s", "model")
  }

  private def initPurchaseService(): PurchaseServiceImpl = {
    val uri = "mongodb://localhost:27017/wazza-test"
    val mongoDbService = new MongoDatabaseService
    mongoDbService.init(uri, PurchaseInfo.PurchaseCollection)
    val userService = new MobileUserServiceImpl(mongoDbService)
    new PurchaseServiceImpl(userService, mongoDbService)
  }

  "Purchase Operations" should {
    running(FakeApplication()) {
      val purchaseService = initPurchaseService
      val purchase = new PurchaseInfo(
        PurchaseData.id,
        PurchaseData.userId,
        PurchaseData.applicationName,
        PurchaseData.itemId,
        PurchaseData.price,
        PurchaseData.time,
        PurchaseData.deviceInfo,
        Some(PurchaseData.location)
      )
    
      "Insert" in {
        purchaseService.save(purchase, PurchaseData.userId) must equalTo(Success())
        purchaseService.exist(PurchaseData.id, PurchaseData.userId) must equalTo(true)
        purchaseService.get(PurchaseData.id, PurchaseData.userId).get must equalTo(purchase)
      }

      "Delete" in {
        purchaseService.delete(purchase, PurchaseData.userId) must equalTo(Success())
        purchaseService.get(PurchaseData.id, PurchaseData.userId) must equalTo(None)
      }
    }
  }
}

  * */
