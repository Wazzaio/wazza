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

dashboard
.controller('NumberSessionsFirstPurchaseController', [
  '$scope',
  '$rootScope',
  'ApplicationStateService',
  'DateModel',
  'DetailedKpiModel',
  'AT1PDateChanged',
  'AT1PPlatformsChanged',
  function (
    $scope,
    $rootScope,
    ApplicationStateService,
    DateModel,
    DetailedKpiModel,
    AT1PDateChanged,
    AT1PPlatformsChanged
  ) {

    var title = "Number of Sessions to first Purchase";
    var KpiId = "sessionsFirstPurchase";

    ApplicationStateService.setPath(title);
    $scope.buildContext(new DetailedKpiModel(DateModel.startDate, DateModel.endDate, title));

    $scope.updateData($scope.context, KpiId, title);
    
    $scope.$on(AT1PDateChanged, function(ev, data) {
      $scope.context.beginDate = DateModel.startDate;
      $scope.context.endDate = DateModel.endDate;
      $scope.updateData($scope.context, KpiId, title);
    });

    $scope.$on(AT1PPlatformsChanged, function(ev, data) {
      $scope.updateData($scope.context, KpiId, title);
    });
}]);

