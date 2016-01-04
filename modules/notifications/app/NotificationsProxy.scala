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

package notifications

import common.actors._
import common.messages._
import akka.actor.{ActorRef, Actor, ActorSystem, Props}
import akka.routing.ActorRefRoutee
import akka.routing.Router
import akka.routing.RoundRobinRoutingLogic
import play.api.libs.concurrent.Akka._
import play.api.Play
import play.api.Logger
import play.api.libs.concurrent.Akka
import play.api.Play.current
import play.api._
import notifications.workers._

class NotificationsProxy(
  system: ActorSystem
) extends Actor with Master[WazzaMessage, MailWorker] {

  private val NUMBER_WORKERS = 1

  override def workersRouter = {
    val routees = Vector.fill(NUMBER_WORKERS) {
      val r = context.actorOf(MailWorker.props)
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def killRouter() = {}

  protected def execute[MailRequest](request: MailRequest) = {
    if(Play.isProd) {
      workersRouter.route(request, sender())
    }
  }

  def receive = masterReceive
}

object NotificationsProxy {

  private var singleton: ActorRef = null

  def getInstance(system: ActorSystem = Akka.system) = {
    if(singleton == null) {
      singleton = system.actorOf(
        NotificationsProxy.props(ActorSystem("notifications")), name = "notifications"
      )
    }
    singleton
  }

  private def props(system: ActorSystem): Props = Props(new NotificationsProxy(system))
}

