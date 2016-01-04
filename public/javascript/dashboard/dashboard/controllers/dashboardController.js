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

dashboard.controller('DashboardController', [
    '$scope',
    '$rootScope',
    "$anchorScroll",
    "$state",
    "$document",
    'ApplicationStateService',
    'GetKPIService',
    "DateModel",
    "KpiModel",
    "$q",
    "SelectedPlatformsChange",
    "DashboardCache",
    "DashboardViewChanges",
    "DashboardShowPlatformDetails",
    "DashboardUpdateValuesOnDateChange",
    "CurrencyChanges",
    function (
        $scope,
        $rootScope,
        $anchorScroll,
        $state,
        $document,
        ApplicationStateService,
        GetKPIService,
        DateModel,
        KpiModel,
        $q,
        SelectedPlatformsChange,
        DashboardCache,
        DashboardViewChanges,
        DashboardShowPlatformDetails,
        DashboardUpdateValuesOnDateChange,
        CurrencyChanges
        ) {
        
        $scope.showDetails = true;
        
        var showHidePlatformDetails = function(ev, data) {
          $scope.showDetails = data.value;
        };
        $scope.$on(DashboardShowPlatformDetails, showHidePlatformDetails);
              
        
        /** Revenue KPIs **/
        $scope.totalRevenue = new KpiModel("Total Revenue", "analytics.revenue");
        $scope.arpu = new KpiModel("Avg Revenue Per User", "analytics.arpu");
        $scope.avgRevSession = new KpiModel("Avg Revenue per Session", "analytics.avgRevenueSession");

        /** Updates KPI revenue on currency change **/
        $rootScope.$on(CurrencyChanges, function() {
          $scope.totalRevenue.currencyUpdate();
          $scope.arpu.currencyUpdate();
          $scope.avgRevSession.currencyUpdate();
        });

        /** User KPIs **/
        $scope.ltv = new KpiModel("Life Time Value", "analytics.ltv");
        $scope.payingUsers = new KpiModel("Paying Users", "analytics.payingUsers");
        $scope.avgPurchasesUser = new KpiModel("Avg Purchases Per User", "analytics.avgPurchasesUser");

        /** Session KPIs **/
        $scope.purchasesPerSession = new KpiModel("Purchases per Session", "analytics.purchasesPerSession");
        $scope.numberSessionsFirstPurchase = new KpiModel("Sessions to First Purchase", "analytics.sessionsFirstPurchase");
        $scope.numberSessionsBetweenPurchases = new KpiModel("Sessions Between Purchases", "analytics.sessionsBetweenPurchase");

        $scope.platforms = ApplicationStateService.selectedPlatforms;
        $scope.$on(SelectedPlatformsChange, function(event, args){
            $scope.platforms = ApplicationStateService.selectedPlatforms
            $scope.updateKPIs();
        });

        $scope.$on(DashboardUpdateValuesOnDateChange, function(ev, args){
            $scope.updateKPIs();
        });

        var getDataFromServer = function() {
          var companyName = ApplicationStateService.getCompanyName();
          var app = ApplicationStateService.getApplicationName();
          var begin = DateModel.formatDate(DateModel.startDate);
          var end = DateModel.formatDate(DateModel.endDate);

          
          GetKPIService.getTotalKpiData(companyName, app, begin, end, "revenue", $scope.platforms, ApplicationStateService.currentApplication.paymentSystems)
            .then(function(res) {$scope.totalRevenue.updateKpiValue(res.data);});

          GetKPIService.getTotalKpiData(companyName, app, begin, end, "arpu", $scope.platforms, ApplicationStateService.currentApplication.paymentSystems)
            .then(function(res) {$scope.arpu.updateKpiValue(res.data);});

          GetKPIService.getTotalKpiData(companyName, app, begin, end, "avgRevenueSession", $scope.platforms, ApplicationStateService.currentApplication.paymentSystems)
            .then(function(res) {$scope.avgRevSession.updateKpiValue(res.data);});

          GetKPIService.getTotalKpiData(companyName, app, begin, end, "ltv", $scope.platforms, ApplicationStateService.currentApplication.paymentSystems)
            .then(function(res) {$scope.ltv.updateKpiValue(res.data);});

          GetKPIService.getTotalKpiData(companyName, app, begin, end, "payingUsers", $scope.platforms, ApplicationStateService.currentApplication.paymentSystems)
            .then(function(res) {$scope.payingUsers.updateKpiValue(res.data);});
            
          GetKPIService.getTotalKpiData(companyName, app, begin, end, "avgPurchasesUser", $scope.platforms, ApplicationStateService.currentApplication.paymentSystems)
            .then(function(res) {$scope.avgPurchasesUser.updateKpiValue(res.data);});

          GetKPIService.getTotalKpiData(companyName, app, begin, end, "purchasesPerSession", $scope.platforms, ApplicationStateService.currentApplication.paymentSystems)
            .then(function(res) {$scope.purchasesPerSession.updateKpiValue(res.data);});

          GetKPIService.getTotalKpiData(companyName, app, begin, end, "sessionsBetweenPurchases", $scope.platforms, ApplicationStateService.currentApplication.paymentSystems)
            .then(function(res) {$scope.numberSessionsBetweenPurchases.updateKpiValue(res.data);});
            
          GetKPIService.getTotalKpiData(companyName, app, begin, end, "sessionsFirstPurchase", $scope.platforms, ApplicationStateService.currentApplication.paymentSystems)
            .then(function(res) {$scope.numberSessionsFirstPurchase.updateKpiValue(res.data);});
        };
        
        $scope.updateKPIs = function(){
            getDataFromServer();
        };

        $scope.switchDetailedView = function(state) {
            var name = state.split(".");
            mixpanel.track("Detailed View", {
              "kpi": name[1]
            });
            $state.go(state);
            $document.scrollTop(-50, 500); //hack
        };

        ApplicationStateService.setPath("Dashboard");

        $scope.updateKPIs();

}]);
