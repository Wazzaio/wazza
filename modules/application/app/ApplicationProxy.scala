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

package application

import common.actors._
import common.messages._
import akka.actor.{ActorRef, Actor, ActorSystem, Props}
import akka.routing.ActorRefRoutee
import akka.routing.Router
import akka.routing.RoundRobinRoutingLogic
import play.api.libs.concurrent.Akka._
import application.messages._
import application.workers._
import play.api.Play
import play.api.Logger
import play.api.libs.concurrent.Akka
import play.api.Play.current
import persistence._
import notifications._
import user._

class ApplicationProxy(
  system: ActorSystem,
  databaseProxy: ActorRef,
  notificationProxy: ActorRef,
  userProxy: ActorRef
) extends Actor with Master[ApplicationMessageRequest, ApplicationWorker] {

  private val NUMBER_WORKERS = 5

  override def workersRouter = {
    val routees = Vector.fill(NUMBER_WORKERS) {
      val r = context.actorOf(ApplicationWorker.props(databaseProxy, notificationProxy, userProxy))
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def killRouter = {}

  protected def execute[ApplicationMessageRequest](request: ApplicationMessageRequest) = {
    workersRouter.route(request, sender())
  }

  def receive = masterReceive
}

object ApplicationProxy {

  private var singleton: ActorRef = null

  def getInstance(system: ActorSystem = Akka.system) = {
    if(singleton == null) {
      singleton = system.actorOf(
        ApplicationProxy.props(
          ActorSystem("application"),
          PersistenceProxy.getInstance(system),
          NotificationsProxy.getInstance(system),
          UserProxy.getInstance(system)
        ), name = "application"
      )
    }
    singleton
  }

  def props(
    system: ActorSystem,
    databaseProxy: ActorRef,
    notificationsProxy: ActorRef,
    userProxy: ActorRef
  ): Props = Props(new ApplicationProxy(system, databaseProxy, notificationsProxy, userProxy))
}
