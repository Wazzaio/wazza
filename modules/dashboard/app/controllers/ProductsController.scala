package controllers.dashboard

import play.api._
import play.api.mvc._
import scala.concurrent._
import ExecutionContext.Implicits.global
import controllers.security._
import com.google.inject._

class ProductsController extends Controller {

  def index() = UserAuthenticationAction {implicit request =>
    Ok(views.html.products.productList())
  }

  def product() = UserAuthenticationAction {implicit request =>
    Ok(views.html.products.productCardInfo())
  }

  def details = UserAuthenticationAction {implicit request =>
    Ok(views.html.products.productDetails())
  }
}

