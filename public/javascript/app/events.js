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

'use strict';

var broadcastEvents = angular.module('Wazza.broadcastEvents',[]);

/** Datepicker changes **/
broadcastEvents.value('RevenueDateChanged','analytics.revenue');
broadcastEvents.value('ArpuDateChanged','analytics.arpu');
broadcastEvents.value('AvgRevenueSessionDateChanged','analytics.avgRevenueSession');
broadcastEvents.value('ATBPDateChanged','analytics.sessionsBetweenPurchase');
broadcastEvents.value('AT1PDateChanged','analytics.sessionsFirstPurchase');
broadcastEvents.value('PurchasesPerUserChanged','analytics.avgPurchasesUser');
broadcastEvents.value('LtvDateChanged','analytics.ltv');
broadcastEvents.value('PayingUsersDateChanged','analytics.payingUsers');
broadcastEvents.value('PurchaseDateChanged','analytics.purchasesPerSession');

/** Platform selection changes **/
broadcastEvents.value("SelectedPlatformsChange", 0);
broadcastEvents.value('RevenuePlatformsChanged','analytics.revenue-platformChange');
broadcastEvents.value('ArpuPlatformsChanged','analytics.arpu-platformChange');
broadcastEvents.value('AvgRevenueSessionPlatformsChanged','analytics.avgRevenueSession-platformChange');
broadcastEvents.value('ATBPPlatformsChanged','analytics.sessionsBetweenPurchase-platformChange');
broadcastEvents.value('AT1PPlatformsChanged','analytics.sessionsFirstPurchase-platformChange');
broadcastEvents.value('PurchasesPerUserPlatformsChanged','analytics.avgPurchasesUser-platformChange');
broadcastEvents.value('LtvPlatformsChanged','analytics.ltv-platformChange');
broadcastEvents.value('PayingUsersPlatformsChanged','analytics.payingUsers-platformChange');
broadcastEvents.value('PurchaseDatePlatformsChanged','analytics.purchasesPerSession-platformChange');

/** Current application changes **/
broadcastEvents.value('CurrentAppChanges', 'appchange');

/** Dashboard view changes **/
broadcastEvents.value("DashboardViewChanges", 1);
broadcastEvents.value("DashboardShowPlatformDetails", 2);

/** Update dates **/
broadcastEvents.value("DashboardUpdateValuesOnDateChange", 3);
broadcastEvents.value("OverviewUpdateValuesOnDateChange", 4);

/** Currency Changes **/
broadcastEvents.value("CurrencyChanges", "newCurrency");
