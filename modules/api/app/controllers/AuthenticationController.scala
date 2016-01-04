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

package controllers.api

import com.google.inject._
import play.api._
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.{JsArray, Json, JsValue}
import play.api.mvc._
import service.security.definitions.TokenManagerService
import service.user.definitions._
import scala.concurrent._
import ExecutionContext.Implicits.global
import controllers.security._
import scala.collection.mutable.Stack
import scala.util.{Try, Success, Failure}
import user._
import user.messages._
import models.common._

class AuthenticationController  extends Controller {

  val UserExistsHeader = "X-UserExists"

  def authentication() = ApiSecurityAction.async(parse.json) {implicit request =>
    val result = request.headers.get(UserExistsHeader) match {
      case Some(_) => {
        //Do nothing..
        true
      }
      case None => {
        //creates mobile user
        val req = new MUCreate(
          false,
          request.companyName,
          request.applicationName,
          (request.body \ "userId").as[String],
          (request.body \ "device").validate[DeviceInfo].asOpt.get,
          new Stack
        )
        UserProxy.getInstance() ! req
        false
      }
    }
    Future.successful(Ok(Json.obj("result" -> result)))
  }
}

