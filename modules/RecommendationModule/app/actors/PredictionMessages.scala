package actors.recommendation

import models.application.Item
import models.user.MobileSession
import models.user.MobileUser
import play.api.libs.json.JsValue

sealed trait PredictionMessages

case class AddUserMessage(user: MobileUser) extends PredictionMessages

case class GetUserMessage(id: String) extends PredictionMessages

case class AddSessionMessage(session: MobileSession) extends PredictionMessages

case class AddItemMessage(item: Item) extends PredictionMessages

case class ResponseMessage(response: JsValue)

case class ErrorMessage(msg: String) extends PredictionMessages