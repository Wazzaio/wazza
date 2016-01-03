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

import common.messages._
import play.api.libs.concurrent.Akka._
import akka.actor.{Actor, Props}
import reflect.runtime.universe._
import akka.routing.Router
import scala.reflect.runtime.universe._
import scala.reflect.{ClassTag}

trait Master[M <: WazzaMessage, W <: Worker[_]] {
  this:  Actor =>

  protected def killRouter()

  protected def workersRouter: Router

  protected def execute[M](request: M)

  def masterReceive(implicit tag: ClassTag[M]): Receive = {
    case msg if tag.runtimeClass.isInstance(msg) => execute(msg.asInstanceOf[M])
  }
}

