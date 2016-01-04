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

package common.actors

import akka.actor.{ActorRef}
import common.messages._
import java.security.SecureRandom
import java.math.BigInteger
import scala.collection.mutable.Map
import scala.collection.mutable.ListBuffer

/**
  S - List of all data that was sent for a given request
  R - List of all received results of that request
**/
case class Element[S](originalRequest: WazzaMessage, sendedData: List[S], sender: ActorRef) {

  private var results = new ListBuffer[Any]()

  def hasReceivedAllResults = sendedData.size == results.size

  def addResult[N](newResult: N) = {
    results += newResult
    results = results.distinct
  }

  def getResults = results.toList
}

class DependencyStorage {



  private val BASE = 32
  private val BITS = 130
  private val random = new SecureRandom
  private def generateId = new BigInteger(BITS, random).toString(32)

  private val storage: Map[String, Element[_]] = Map()

  def store[S](sender: ActorRef, sendedData: List[S], originalReq: WazzaMessage): String = {
    val id = generateId
    storage += (id -> new Element[S](originalReq, sendedData, sender))
    id
  }

  def gotAllResults(hash: String): Boolean = {
    storage.get(hash) match {
      case Some(entry) => entry.hasReceivedAllResults
      case _ => false
    }
  }

  def saveResult[R](hash: String, result: R) = {
    storage.get(hash) match {
       case Some(entry) => entry.addResult(result)
      case _ => {
        //TODO log error and launch exception
      }
    }
  }

  def get[S](hash: String): Option[Element[S]] = {
    val res = storage.get(hash) match {
      case Some(element) => Some(element.asInstanceOf[Element[S]])
      case _ => None
    }
    storage -= hash
    res
  }

  def getOriginalRequest(hash: String): Option[WazzaMessage] = {
    storage.get(hash) match {
      case Some(element) => Some(element.originalRequest)
      case _ => None
    }
  }
}

trait DependencyStorageDecorator {
  val dependencyStorage = new DependencyStorage
}

