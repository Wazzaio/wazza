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

/**
  NOT USED AT THE MOMENT

package controllers.dashboard

import play.api._
import play.api.mvc._
import scala.concurrent._
import ExecutionContext.Implicits.global
import controllers.security._
import service.security.definitions.{TokenManagerService}
import service.application.definitions._
import service.user.definitions._
import com.google.inject._
import play.api.libs.json._
import models.application._
import java.util.Date


class InventoryController @Inject()(
	applicationService: ApplicationService,
	userService: UserService
	) extends Controller {

	def bootstrapInventory() = UserAuthenticationAction.async {implicit request =>
	    userService.getApplications(request.userId) flatMap {applications =>
	        if(applications.isEmpty){
	            //TODO: do not send bad request but a note saying that we dont have applications. then redirect to new application page
	            Future.successful(Forbidden)
	        } else {
	            userService.find(request.userId) flatMap {userOpt =>
	                val user = userOpt.get
	                val companyName = user.company
	                val info = applicationService.find(companyName, applications.head) map {optApp =>
	                    (optApp map {application =>
	                        Json.obj(
	                            "virtualCurrencies" -> new JsArray(application.virtualCurrencies map {vc =>
	                                VirtualCurrency.buildJson(vc)
	                            })/**,
	                            "items" -> new JsArray(applicationService.getItems(companyName, application.name) map {item =>
	                                Item.convertToJson(item)
	                            })**/
	                        )
	                    }).get
	                }
	                info map {Ok(_)}
	            }
	        }
	    }
	}

	//inventory
	def inventory = UserAuthenticationAction {implicit request =>
		Ok(views.html.inventory.inventory())
	}

	def inventoryCRUD = UserAuthenticationAction {implicit request =>
		Ok(views.html.inventory.inventoryCRUD())
	}

	def inventoryVirtualCurrencies = UserAuthenticationAction {implicit request =>
		Ok(views.html.inventory.inventoryVirtualCurrencies())
	}

}

  * */
