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

package persistence

import common.actors._
import common.messages._
import akka.actor.{ActorRef, Actor, ActorSystem, Props}
import akka.routing.ActorRefRoutee
import akka.routing.Router
import akka.routing.RoundRobinRoutingLogic
import play.api.libs.concurrent.Akka._
import persistence.messages._
import persistence.worker._
import com.mongodb.casbah.Imports._
import play.api.Play
import play.api.Logger
import play.api.libs.concurrent.Akka
import play.api.Play.current

class PersistenceProxy (
  system: ActorSystem
) extends Actor with Master[PersistenceMessage, PersistenceWorker] {

  private val NUMBER_WORKERS = 5

  override def workersRouter = {
    val routees = Vector.fill(NUMBER_WORKERS) {
      val r = context.actorOf(Props[PersistenceWorker])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def killRouter() = {}

  override def preStart() = {
    super.preStart()
  }

  override def postStop() = {
    MongoFactory.destroy
  }

  protected def execute[PersistenceMessage](request: PersistenceMessage) = {
    workersRouter.route(request, sender())
  }

  def receive = masterReceive
}

protected[persistence] object MongoFactory {

  private def getMongoURI() = {
    Play.current.configuration.getString("mongodb.uri") match {
      case Some(str) => {
        Some(MongoClientURI(str))
      }
      case _ => None
    }
  }

  private def getMongoClient() = {
    getMongoURI() match {
      case Some(uri) => MongoClient(uri)
      case _ => throw new Exception("")
    }
  }

  private var _client: MongoClient = null

  private def client: MongoClient = {
    if(_client == null) {
      _client = getMongoClient()
    }
    _client
  }

  def getCollection(collectionName: String, debug: Boolean = false) =  {
    val db = getMongoURI().get.database.get
    client(db)(collectionName)
  }

  def destroy() = {
    Logger.info("Destroying mongodb connection")
    client.close
  }
}

object PersistenceProxy {

  private var singleton: ActorRef = null

  private def props(system: ActorSystem): Props = Props(new PersistenceProxy(system))

  def getInstance(system: ActorSystem = Akka.system) = {
    if(singleton == null){
      singleton = system.actorOf(PersistenceProxy.props(ActorSystem("Persistence")), name = "persistence")
    }
    singleton
  }
}

