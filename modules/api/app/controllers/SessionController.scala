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

import models.user.MobileSession
import org.joda.time.Seconds
import play.api._
import play.api.libs.json.JsError
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.{JsArray, Json}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Failure
import scala.util.Success
import org.joda.time.format.DateTimeFormat
import org.joda.time.Interval
import scala.concurrent._
import ExecutionContext.Implicits.global
import persistence.utils._
import controllers.security._
import user._
import user.messages._
import scala.collection.mutable.Stack
import scala.util.{Try, Success, Failure}

class SessionController extends Controller {

  private def createSession(
    content: JsValue,
    companyName: String,
    applicationName: String
  ): Try[Unit] = {
    val start = DateUtils.buildJodaDateFromString((content \ "startTime").as[String])
    val end = DateUtils.buildJodaDateFromString((content \ "endTime").as[String])

    try {
      val session = MobileSession.buildFromJson(Json.obj(
        "id" -> (content \ "hash").as[String],
        "userId" -> (content \ "userId").as[String],
        "length" -> (new Interval(start, end).toDurationMillis() / 1000.0),
        "startTime" -> (content \ "startTime").as[String],
        "device" -> (content \ "deviceInfo"),
        "purchases" -> (content \ "purchases").as[List[String]])
      )
      val userProxy = UserProxy.getInstance()
      val request = new SRSave(new Stack, companyName, applicationName, session)
      userProxy ! request
      new Success
    } catch {
      case ex: Exception => new Failure(ex)
    }
  }

  def saveSession() = ApiSecurityAction.async(parse.json) {implicit request =>
    val companyName = request.companyName
    val applicationName = request.applicationName
    val content = (Json.parse((request.body \ "content").as[String].replace("\\", "")) \ "session").as[JsArray]

    val res = content.value.map{(sessionJson: JsValue) =>
      createSession(sessionJson, companyName, applicationName)
    }

    def hasFailures[A](xs: Seq[Try[A]]) =
      Try(xs.map(_.get))

    hasFailures[Unit](res) match {
      case Success(_) => Future.successful(Ok)
      case Failure(_) => Future.successful(InternalServerError("Invalid session json"))
    }
  }
}

