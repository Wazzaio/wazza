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

package controllers.dashboard

import play.api._
import play.api.mvc._
import scala.concurrent._
import ExecutionContext.Implicits.global
import controllers.security._
import service.security.definitions.{TokenManagerService}
import com.google.inject._
import play.api.libs.json._
import models.application._
import java.util.Date
import akka.util.{Timeout}
import akka.pattern.ask
import user._
import user.messages._
import application._
import application.messages._
import scala.concurrent.duration._
import scala.collection.mutable.Stack

class OverviewController extends Controller {

  private implicit val timeout = Timeout(10 seconds)
  private val userProxy = UserProxy.getInstance()
  private val appProxy = ApplicationProxy.getInstance()

  def bootstrapOverview() = UserAuthenticationAction.async {implicit request =>
    val appsFuture = (userProxy ? new URGetApplications(new Stack, request.userId, true)).mapTo[URApplicationsResponse]
    appsFuture flatMap {applications =>
      if(applications.res.isEmpty) {
        Future.successful(Ok(new JsArray(List())))
      } else {
        val userFuture = (userProxy ? new URFind(new Stack, request.userId, true)).mapTo[UROptionResponse]
        userFuture flatMap {user =>
          val companyName = user.res.get.company
          val apps = Future.sequence(applications.res map {appId: String =>
            val futureApp = (appProxy ? new ARFind(new Stack, companyName, appId, true)).mapTo[AROptionResponse]
              futureApp map {optApp =>
                (optApp.res map {application =>
                  Json.obj(
                    "name" -> application.name,
                    "url" -> application.imageName,
                    "platforms" -> application.appType,
                    "paymentSystems" -> application.paymentSystems
                  )
                }).get
              }
          })
          apps map {appsList =>
            Ok(new JsArray(appsList))
          }
        }
      }
    }
  }

  def company() = UserAuthenticationAction.async {implicit request =>
    val userFuture = (userProxy ? new URFind(new Stack, request.userId, true)).mapTo[UROptionResponse]
    userFuture flatMap {user =>
      val companyName = user.res.get.company
      Future.successful(Ok(Json.obj("name" -> companyName)))
    }
  }

  def overview() = UserAuthenticationAction {implicit request =>
    Ok(views.html.overview())
  }
}
