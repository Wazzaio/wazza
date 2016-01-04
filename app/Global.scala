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

import play.api.{GlobalSettings, Application}
import com.google.inject._
import play.api._
import play.api.Play
import play.api.Play.current
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent.Future
import service.user.definitions._
import service.user.implementations._
import service.user.modules._
import service.security.modules._
import service.aws.modules._
import service.persistence.modules.PersistenceModule
import service.analytics.modules.AnalyticsModule
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.concurrent.Akka
import akka.actor.{ActorRef, Actor, ActorSystem, Kill, Props}
import persistence._
import application._
import user._
import java.io.{StringWriter, PrintWriter}
import scala.concurrent._
import scala.concurrent.duration._
import notifications._
import notifications.messages._
import payments._
import play.filters.headers.SecurityHeadersFilter
import java.util.TimeZone
import play.api.Logger

object Global extends WithFilters(SecurityHeadersFilter()) with GlobalSettings {

  private var modulesProxies = List[ActorRef]()

  /**
    Creates modules system's and proxies
  **/
  override def onStart(app: Application) = {
    Logger.info("Running on Timezone: " + TimeZone.getDefault())
    val databaseProxy = PersistenceProxy.getInstance()
    val userProxy = UserProxy.getInstance()
    val applicationProxy = ApplicationProxy.getInstance()
    val notificationsProxy = NotificationsProxy.getInstance()
    modulesProxies = List(databaseProxy, userProxy, applicationProxy, notificationsProxy)
  }

  // 500 - internal server error
  override def onError(request: RequestHeader, throwable: Throwable) = {
    if(Play.isProd) {
      val sw = new StringWriter()
      val pw = new PrintWriter(sw)
      throwable.printStackTrace(pw)
      val stack = sw.toString()
      val msg = s"Message: ${throwable.getMessage}\nStack Trace: ${stack}"
      val request = new SendEmail(null, List("joao@wazza.io", "duarte@wazza.io"), "500 ERROR", msg)
      NotificationsProxy.getInstance() ! request
    }

    Future.successful(InternalServerError(views.html.errorPage()))
  }
  /**
    Shutdowns all modules' systems and actors
  **/
  override def onStop(app: Application) = modulesProxies.foreach{_ ! Kill}

  /**
    Dependency injection setup
  **/
  private lazy val injector = {
    Guice.createInjector(
      new PersistenceModule,
      new UserModule,
      new SecurityModule,
      new AWSModule,
      new PersistenceModule,
      new AnalyticsModule,
      new PaymentsModule
    )
  }

  override def getControllerInstance[A](clazz: Class[A]) = {
    injector.getInstance(clazz)
  }

  override def onHandlerNotFound(request: RequestHeader) =  {
    if(Play.isProd && !(request.path contains "php") && !(request.path contains "cgi")) {
      val msg = s"Trying to access path: ${request.path}"
      val mailRequest = new SendEmail(null, List("joao@wazza.io", "duarte@wazza.io"), "4xx ERROR", msg)
      NotificationsProxy.getInstance() ! mailRequest
    }

    Future.successful(NotFound(
      views.html.index()
    ))
  }
}

