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

class DashboardController @Inject()(
  applicationService: ApplicationService,
  userService: UserService
  ) extends Controller {

  def index() = UserAuthenticationAction {implicit request =>
    Ok(views.html.dashboard())
  }

  def kpi = UserAuthenticationAction {implicit request =>
    Ok(views.html.kpi())
  }

}
