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

package payments.paypal

import play.api.libs.json._
import scala.concurrent._
import ExecutionContext.Implicits.global
import play.api.Play.current
import play.api.libs.ws._
import play.api.libs.ws.ning.NingAsyncHttpClientConfigBuilder
import play.api.http.Status

class PayPalServiceImpl extends PayPalService {

  private val PAY_PAL_URL = "https://api.sandbox.paypal.com/v1/"

  def getAccessToken(clientID: String, secret: String): Future[String] = {
    val futureResult = WS.url(PAY_PAL_URL + "oauth2/token")
      .withHeaders("Content-type" -> "application/x-www-form-urlencoded")
      .withAuth(clientID, secret, WSAuthScheme.BASIC)
      .withFollowRedirects(true)
      .post("grant_type=client_credentials")

    futureResult map {response =>
      if(response.status == Status.OK) {
        (response.json \ "access_token").as[String]
      } else {
        null
      }
    }
  }

  def verifyPayment(accessToken: String, paymentID: String, amount: Double, currency: String): Future[Boolean] = {
    val verificationUrl = s"payments/payment/${paymentID}"
    val futureResult =  WS.url(PAY_PAL_URL + verificationUrl)
      .withHeaders("Content-type" -> "application/x-www-form-urlencoded")
      .withHeaders("Authorization" -> s"Bearer ${accessToken}")
      .get

    futureResult map {result =>
      if(result.status == Status.OK) {
        true
      } else {
        false
      }
    }
  }
}

