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

package service.user.definitions

import models.user.MobileSession
import play.api.libs.json.JsValue
import scala.util.Try
import scala.concurrent._

trait MobileSessionService {

  def create(json: JsValue): Try[MobileSession]

  def insert(companyName: String, applicationName: String, session: MobileSession): Future[Unit]

  def get(hash: String): Future[Option[MobileSession]]

  def exists(id: String): Future[Boolean]

  def calculateSessionLength(session: MobileSession, dateStr: String): Future[Unit]
}

