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

package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import controllers.security._
import service.security.definitions.{TokenManagerService}
import com.google.inject._

class Application extends Controller {

  def index = Action {
	  Ok(views.html.index())
  }

  def home = Action {
    Ok(views.html.home())
  }

  def analyticsframe = Action {
    Ok(views.html.analyticsFrame())
  }

  def webframe = Action {
    Ok(views.html.webframe())
  }

  def notavailableyet = Action {
    Ok(views.html.notavailableyet())
  }

  def httpError = Action {
    Ok(views.html.errorPage())
  }

  def terms = Action {
    Ok(views.html.terms())
  }

  def privacy = Action {
    Ok(views.html.privacy())
  }

}

