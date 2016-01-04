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
.controller('AnalyticsController', [
  '$scope',
  '$rootScope',
  'ApplicationStateService',
  'GetKPIService',
  'DateModel',
  'DetailedKpiModel',
  '$cookieStore',
  function (
    $scope,
    $rootScope,
    ApplicationStateService,
    GetKPIService,
    DateModel,
    DetailedKpiModel,
    $cookieStore
  ) {

    $scope.toggle = true;

     /**
     * Sidebar Toggle & Cookie Control
     */
    var mobileView = 992;

    $scope.getWidth = function() {
        return window.innerWidth;
    };
      
    $scope.$watch($scope.getWidth, function(newValue, oldValue) {
      if (newValue >= mobileView) {
        if (angular.isDefined($cookieStore.get('toggle'))) {
          $scope.toggle = ! $cookieStore.get('toggle') ? false : true;
        } else {
          $scope.toggle = true;
        }
      } else {
        $scope.toggle = false;
      }
    });

    window.onresize = function() {
      $scope.$apply();
    };

    $scope.buildContext = function(model) {
      $scope.context = model;
    };
      
    $rootScope.$on('sidebar', function(ev, data) {
      $scope.toggle = !$scope.toggle;
      $cookieStore.put('toggle', $scope.toggle);
    });
      
    $scope.updateData = function(context, KpiId, label) {
      updateChartData(context, KpiId, label);
      updateTotalValues(context, KpiId);
    };

    var updateChartData = function(context, KpiId, label) {
      GetKPIService.getDetailedKPIData(
        ApplicationStateService.getCompanyName(),
        ApplicationStateService.getApplicationName(),
        DateModel.formatDate(context.beginDate),
        DateModel.formatDate(context.endDate),
        KpiId,
        ApplicationStateService.selectedPlatforms,
        ApplicationStateService.currentApplication.paymentSystems  
      ).then(function(results) {
        kpiDataSuccessHandler(results, context, label);
      },function(err) {console.log(err);}
      );
    };

    var updateTotalValues = function(context, KpiId) {
      GetKPIService.getTotalKpiData(
        ApplicationStateService.getCompanyName(),
        ApplicationStateService.getApplicationName(),
        DateModel.formatDate(context.beginDate),
        DateModel.formatDate(context.endDate),
        KpiId,
        ApplicationStateService.selectedPlatforms,
        ApplicationStateService.currentApplication.paymentSystems
      ).then(function(results) {
        totalValueHandler(context, results);
      },function(err) {console.log(err);}
      );
    };

    var totalValueHandler = function(context, data) {
      context.model.updateKpiValue(data.data);
    };

    var kpiDataSuccessHandler = function(data, context, label) {
      context.updateChartData(data, ApplicationStateService.selectedPlatforms);
    };
  }]);

