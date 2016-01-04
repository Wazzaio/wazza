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

import models.user.{User}
import scala.concurrent._
import ExecutionContext.Implicits.global

trait UserService {

  def insertUser(user: User): Future[Unit]

  def find(email: String): Future[Option[User]]

  def exists(email: String): Future[Boolean]

  def deleteUser(user: User): Future[Unit]

  def validateUser(email: String): Future[Boolean] = {
    this.exists(email) map { exist => !exist}
  }

  def addApplication(email: String, applicationId: String): Future[Unit]

  def getApplications(email: String): Future[List[String]]

  def authenticate(email: String, password: String): Option[User]

}
