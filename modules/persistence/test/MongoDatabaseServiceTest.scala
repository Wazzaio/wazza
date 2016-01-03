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

package test.persistence
/**
import org.specs2.mutable._
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._
import com.google.inject._
import scala.util.Failure
import scala.util.Success
import service.persistence.definitions.DatabaseService
import service.persistence.modules.PersistenceModule

class MongoDatabaseServiceTest  extends Specification {

  private val databaseService = Guice.createInjector(new PersistenceModule).getInstance(classOf[DatabaseService])
  private val uri = "mongodb://localhost:27017/wazza-test"

  "MongoDB Basic operations" should {
    running(FakeApplication()) {
      databaseService.init(uri, "persistence")
      val user = Json.obj("email" -> "test@gmail.com", "name" -> "test", "applications" -> List[JsObject]())

      "Insert" in  {
        databaseService.insert(user) must equalTo(Success())
      }

      "Delete" in  {
        databaseService.delete(user) must equalTo(Success())
      }
      
      "Update" in  {
        databaseService.insert(user)
        databaseService.update("email", "test@gmail.com", "name", "changedName")
        val u = databaseService.get("email", "test@gmail.com")
        databaseService.delete(user)
        (u.get \ "name").as[String]  must equalTo("changedName")
      }
    }
  }

  "MongoDB Array operations" should {
    running(FakeApplication()) {
      val user = Json.obj("email" -> "test@gmail.com", "name" -> "test", "applications" -> List[JsObject]())
      val el = Json.obj("name" -> "app test")

      "Insert" in {
        databaseService.addElementToArray[JsObject](
          "email",
          "test@gmail.com",
          "applications",
          el
        ) must equalTo(Success())
      }

      "Update" in {
        val res = databaseService.updateElementOnArray[String](
         "email",
          "test@gmail.com",
          "applications",
          "name",
          "app test",
          "app test 3"
        ) match {
          case Success(_) => true
          case Failure(_) => false
        }

        res must equalTo(true)
      }

      "Delete" in {
        val res = databaseService.deleteElementFromArray[String](
          "email",
          "test@gmail.com",
          "applications",
          "name",
          (el \ "name").as[String]
        ) match {
          case Success(_) => true
          case Failure(_) => false
        }

        val element = databaseService.getElementFromArray[JsObject](
          "email",
          "test@gmail.com",
          "applications",
          "name",
          el
        )

        databaseService.dropCollection()
        res must equalTo(true)
      }
    }
  }
}
  * */
