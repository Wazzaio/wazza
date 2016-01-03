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

// package service.user.implementations

// import java.text.SimpleDateFormat
// import java.util.concurrent.TimeUnit
// import play.api.libs.json._
// import scala.util.Failure
// import scala.util.Success
// import service.user.definitions.MobileSessionService
// import models.user.MobileSession
// import play.api.libs.json.JsValue
// import scala.util.Try
// import com.google.inject._
// import service.persistence.definitions.DatabaseService
// import models.user.MobileSessionInfo
// import scala.concurrent._
// import ExecutionContext.Implicits.global
// import persistence.utils._

// class MobileSessionServiceImpl @Inject()(
//   databaseService: DatabaseService
// ) extends MobileSessionService {

//   def create(json: JsValue): Try[MobileSession] = {
//     (json).validate[MobileSession] match {
//       case session: JsSuccess[MobileSession] => {
//         Success(session.get)
//       }
//       case e: JsError => new Failure(new Exception(JsError.toFlatJson(e).toString))
//     }
//   }

//   def insert(companyName: String, applicationName: String, session: MobileSession): Future[Unit] = {
//     val promise = Promise[Unit]

//     val insertFuture = exists(session.id) flatMap { res =>
//       if(!res) {
//         val collection = MobileSession.getCollection(companyName, applicationName)
//         databaseService.insert(collection, Json.toJson(session)) flatMap {r =>
//           addSessionToHashCollection(companyName, applicationName, session)
//         }
//       } else {
//         Future {new Exception("mobile session alreayd exists") }
//       }
//     }

//     insertFuture map {res=>
//       promise.success()
//     } recover {
//       case ex: Exception => promise.failure(ex)
//     }
//     promise.future
//   }

//   def get(hash: String): Future[Option[MobileSession]] = {
//     getSessionInfo(hash) flatMap {optSessionInfo =>
//       optSessionInfo match {
//         case Some(info) => {
//           val collection = MobileSession.getCollection(info.companyName, info.applicationName)
//           databaseService.get(collection, MobileSession.Id, hash) map {opt =>
//             opt match {
//               case Some(json) => {
//                 json.validate[MobileSession].fold(
//                   valid = { s => Some(s) },
//                   invalid = {_ => None}
//                 )
//               }
//               case None => None
//             }
//           }
//         }
//         case None => Future{ None }
//       }
//     }
//   }

//   private def addSessionToHashCollection(
//     companyName: String,
//     applicationName: String,
//     session: MobileSession
//   ): Future[Unit] = {
//     val promise = Promise[Unit]
//     val collection = MobileSessionInfo.collection
//     val info = new MobileSessionInfo(
//       session.id,
//       session.userId,
//       applicationName,
//       companyName
//     )
//     databaseService.insert(collection, Json.toJson(info)) map {res =>
//       promise.success()
//     } recover {
//       case ex: Exception => promise.failure(ex)
//     }
//     promise.future
//   }

//   private def getSessionInfo(id: String): Future[Option[MobileSessionInfo]] = {
//     exists(id) flatMap {res =>
//       if(res) {
//         databaseService.get( MobileSessionInfo.collection, MobileSessionInfo.Id, id) map {sessionOpt =>
//           sessionOpt match {
//             case Some(json) => {
//               json.validate[MobileSessionInfo].fold(
//                 valid = { s => Some(s) },
//                 invalid = {_ => None}
//               )
//             }
//             case None => None
//           }
//         }
//       } else Future{
//         None
//       }
//     }
//   }

//   def exists(id: String): Future[Boolean] = {
//     val collection = MobileSessionInfo.collection
//     databaseService.exists(collection, MobileSessionInfo.Id,id)
//   }

//   def calculateSessionLength(session: MobileSession, dateStr: String): Future[Unit] = {
//     val start = session.startTime
//     val end = DateUtils.buildDateFromString(dateStr)
//     val duration =  TimeUnit.MILLISECONDS.toSeconds(end.getTime - start.getTime)

//     getSessionInfo(session.id) flatMap {sessionOpt =>
//       val collection = MobileSession.getCollection(sessionOpt.get.companyName, sessionOpt.get.applicationName)
//       databaseService.update(
//         collection,
//         MobileSession.Id,
//         session.id,
//         "sessionLength",
//         duration
//       )
//     }
//   }
// }

