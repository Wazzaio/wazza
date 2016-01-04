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

package service.user.implementations

import org.bson.types.ObjectId
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import scala.util.Failure
import scala.util.Success
import scala.util.Try
import service.user.definitions.MobileUserService
import models.user.{MobileUser}
import models.user.{MobileSession}
import models.common.{DeviceInfo}
import scala.language.implicitConversions
import com.google.inject._
import service.persistence.definitions.{DatabaseService}
import play.api.libs.json.Json
import models.payments.PurchaseInfo
import java.util.Date
import scala.concurrent._
import ExecutionContext.Implicits.global

class MobileUserServiceImpl @Inject()(
  databaseService: DatabaseService
) extends MobileUserService {

  def createMobileUser(
    companyName: String,
    applicationName: String,
    userId: String
  ): Future[Unit] = {
    val collection = MobileUser.getCollection(companyName, applicationName)
    exists(companyName, applicationName, userId) flatMap {userExists =>
      if(!userExists) {
        val user = new MobileUser(userId, List(), List(), List())
        databaseService.insert(collection, user) map {res => res}
      } else {
        Future {new Exception("Duplicated mobile user")}
      }
    }
  }

  def get(companyName: String, applicationName: String, userId: String): Future[Option[MobileUser]] = {
    val collection = MobileUser.getCollection(companyName, applicationName)
    databaseService.get(collection, MobileUser.KeyId, userId) map {opt =>
      opt match {
        case Some(j) => Some(j)
        case None => None
      }
    }
  }

  def exists(companyName: String, applicationName: String, userId: String): Future[Boolean] = {
    val collection = MobileUser.getCollection(companyName, applicationName)
    databaseService.exists(collection, MobileUser.KeyId, userId)
  }
}

