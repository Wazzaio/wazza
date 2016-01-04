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
package controllers.api

import play.api._
import play.api.Play.current
import play.api.mvc._
import scala.concurrent._
import ExecutionContext.Implicits.global
import controllers.security._
import service.security.definitions.{TokenManagerService}
import models.application._
import service.application.definitions._
import service.user.definitions._
import com.google.inject._
import scala.math.BigDecimal
import models.user._
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.DurationFieldType
import org.joda.time.DateTime
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scala.util.Random
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class GenerateSessionsController @Inject()(
  applicationService: ApplicationService,
  userService: UserService,
  purchaseService: PurchaseService,
  mobileSessionService: MobileSessionService
) extends Controller {

  private lazy val NumberMobileUsers = 70

  private def generateSessions(companyName: String, applicationName: String): Future[Boolean] = {
    val end = new LocalDate()
    val start = end.minusDays(7)
    val days = Days.daysBetween(start, end).getDays() + 1

    println(s"START $start | END $end")
    println(days)
    val result = List.range(0, days) map {index =>
      val currentDay = start.withFieldAdded(DurationFieldType.days(), index)
      println(s"CURRENT DAY $currentDay")
      Future.sequence((1 to NumberMobileUsers) map {userNumber =>
        val session = new MobileSession(
          (s"${currentDay.toString}-$userNumber"), //hash
          userNumber.toString,
          2,
          currentDay.toDate,
          new DeviceInfo("osType", "name", "version", "model"),
          List[String]() //List of purchases id's
        )
        mobileSessionService.insert(companyName, applicationName, session)
      }) map {a => a.head}
    }
    Future.sequence(result) map {a => println(a) ; true}// map {a => println("Setup done!")}
  }

  def execute(companyName: String, applicationName: String) = Action.async {
    generateSessions(companyName, applicationName) map {r =>
      Ok
    }
  }
}

  * */
