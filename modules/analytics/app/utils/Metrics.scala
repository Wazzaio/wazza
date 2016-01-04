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

package utils.analytics

object Metrics {

  def totalRevenueCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_TotalRevenue_${applicationName}"

  def avgSessionLengthCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_SessionLength_${applicationName}"

  def payingUsersCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_payingUsers_${applicationName}"

  def activeUsersCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_activeUsers_${applicationName}"

  def numberSessionsCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_numberSessions_${applicationName}"

  def mobileSessionsCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_mobileSessions_${applicationName}"

  def numberSessionsPerUserCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_numberSessionsPerUser_${applicationName}"

  def arpuCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_Arpu_${applicationName}"

  def avgRevenueSessionCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_avgRevenueSession_${applicationName}"

  def avgPurchasesUserCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_avgPurchasesUser_${applicationName}"

  def avgSessionsPerUserCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_numberSessionsPerUser_${applicationName}"

  def lifeTimeValueCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_LifeTimeValue_${applicationName}"

  def sessionsBetweenPurchasesCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_NumberSessionsBetweenPurchases_${applicationName}"

  def averagePurchasePerSessionCollection(
    companyName: String,
    applicationName: String
  ) = s"${companyName}_PurchasesPerSession_${applicationName}"

  def sessionsFirstPurchase(
    companyName: String, 
    applicationName: String
  ) = s"${companyName}_NumberSessionsFirstPurchase_${applicationName}"
}

