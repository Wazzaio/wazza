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

package persistence.messages

import common.messages._
import akka.actor.{ActorRef}
import scala.collection.mutable.Stack
import java.util.Date
import org.bson.types.ObjectId
import play.api.libs.json.JsObject
import play.api.libs.json.JsValue

trait PersistenceMessage extends WazzaMessage {
  def direct: Boolean
}

case class Exists(
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  key: String,
  value: String,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

case class Get(
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  key: String,
  value: String,
  projection: String = null,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

case class GetListElements(
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  key: String,
  value: String,
  projection: String = null,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

case class GetElementsWithoutArrayContent(
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  arrayKey: String,
  elementKey: String,
  array: List[String],
  limit: Int,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

case class GetCollectionElements(
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

case class Insert(
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  model: JsValue,
  extra: Map[String, ObjectId] = null,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

case class Delete(
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  el: JsValue,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

case class Update(
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  key: String,
  keyValue: String,
  valueKey: String,
  newValue: Any,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

/**
  Time-ranged queries
**/
case class GetDocumentsWithinTimeRange(
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  dateFields: Tuple2[String, String],
  start: Date,
  end: Date,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

case class GetDocumentsByTimeRange(
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  dateField: String,
  start: Date,
  end: Date,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

/**
  Array operations
  **/

case class ExistsInArray[T <: Any](
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  docIdKey: String,
  docIdValue: String,
  arrayKey: String,
  elementKey: String,
  elementValue: T,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

case class GetElementFromArray[T <: Any](
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  docIdKey: String,
  docIdValue: String,
  arrayKey: String,
  elementKey: String,
  elementValue: T,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

case class GetElementsOfArray(
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  docIdKey: String,
  docIdValue: String,
  arrayKey: String,
  limit: Option[Int],
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

case class AddElementToArray[T <: Any](
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  docIdKey: String,
  docIdValue: String,
  arrayKey: String,
  model: T,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

case class DeleteElementFromArray[T <: Any](
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  docIdKey: String,
  docIdValue: String,
  arrayKey: String,
  elementKey: String,
  elementValue:T,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

case class UpdateElementOnArray[T](
  var sendersStack: Stack[ActorRef],
  collectionName: String,
  docIdKey: String,
  docIdValue: String,
  arrayKey: String,
  elementId: String,
  elementIdValue: String,
  m: T,
  direct: Boolean = false,
  hash: String = null
) extends PersistenceMessage

