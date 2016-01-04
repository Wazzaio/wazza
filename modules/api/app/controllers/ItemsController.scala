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


/**
  NOT USED AT THE MOMENT

package controllers.api

import com.google.inject._
import models.application.Item
import models.user.LocationInfo
import play.api._
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.{JsArray, Json}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import service.application.definitions.ApplicationService

class ItemsController @Inject()(
  applicationService: ApplicationService
) extends Controller {

  private lazy val OffsetHeader = "Offset"

  private def getOffsetValue[A](request: Request[A]): Int = {
    request.headers.get(OffsetHeader) match {
      case Some(o) => o.toInt
      case None => 0
    }
  }

  def getItems(companyName: String, applicationName: String) = Action.async {implicit request =>
    val futureResult = applicationService.getItems(companyName, applicationName, getOffsetValue(request))
    futureResult map {items =>
      Ok(new JsArray(items map {i => Json.obj("id" -> i.name)}))
    }
  }

  def getItemsWithDetails(companyName: String, applicationName: String) = Action.async  {implicit request =>
    val futureResult = applicationService.getItems(companyName, applicationName, getOffsetValue(request))
    futureResult map {items =>
      Ok(new JsArray(items map {i =>
        Item.convertToJson(i)
      }))
    }
  }

  def getItemDetails(
    companyName: String,
    applicationName:String,
    id: String
  ) = Action.async {implicit request =>
    val futureRes = applicationService.getItem(companyName, id, applicationName)

    futureRes map {opt =>
      Ok((opt map {i =>
        Json.obj("item" -> Item.convertToJson(i))
      }).get)
    }
  }
}

  * */
