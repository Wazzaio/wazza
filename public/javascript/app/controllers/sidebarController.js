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

application.controller('SidebarController', [
  '$scope',
  '$rootScope',
  '$state',
  '$document',
  'ApplicationStateService',
  '$cookieStore',
  function(
    $scope,
    $rootScope,
    $state,
    $document,
    ApplicationStateService,
    $cookieStore
    ) {

    $scope.barState = true;
    $scope.toggleSidebar = function() {
      $scope.barState = !$scope.barState;
      $rootScope.$broadcast('sidebar');
    };
      
    $scope.selectDashboardSection = function(sectionId) {
      $scope.followLink("analytics.dashboard");
      //scroll to top
      var someElement = angular.element(document.getElementById(sectionId));
      $document.scrollToElement(someElement, 0, 500);
    };

    $scope.followLink = function(state){
      if(ApplicationStateService.getApplicationName() === ""){
        swal("No Application Selected", "Please select an application")
        $state.go("analytics.overview");
      }
      else if(state !== $state.current.name){
        $state.go(state);
        $document.scrollTop(-50, 500); //hack
      }
    };

    function SidebarOptions(name, link, css) {
      this.name = name;
      this.link = link;
      this.css = css;
    };

    $scope.dashboardOptions = [];
    $scope.dashboardOptions.push(new SidebarOptions("Revenue", "analytics.dashboard", ""));
    $scope.dashboardOptions.push(new SidebarOptions("Users", "analytics.dashboard", ""));
    $scope.dashboardOptions.push(new SidebarOptions("Sessions", "analytics.dashboard", ""));

    $scope.analyticsOptions = [];
    /** Revenue **/
    $scope.analyticsOptions.push(new SidebarOptions("Revenue", "analytics.dashboard", "childIcon fa fa-shopping-cart"));
    $scope.analyticsOptions.push(new SidebarOptions("Total Revenue", "analytics.revenue", ""));
    $scope.analyticsOptions.push(new SidebarOptions("Avg Revenue Per User", "analytics.arpu", ""));
    $scope.analyticsOptions.push(new SidebarOptions("Avg Revenue Per Session", "analytics.avgRevenueSession", ""));

    /** Users **/
    $scope.analyticsOptions.push(new SidebarOptions("Users", "analytics.dashboard", "childIcon fa fa-user"));
    $scope.analyticsOptions.push(new SidebarOptions("Life Time Value", "analytics.ltv", ""));
    $scope.analyticsOptions.push(new SidebarOptions("Paying Users", "analytics.payingUsers", ""));
    $scope.analyticsOptions.push(new SidebarOptions("Avg Purchases Per User", "analytics.avgPurchasesUser", ""));

    /** Sessions **/
    $scope.analyticsOptions.push(new SidebarOptions("Sessions", "analytics.dashboard", "childIcon fa fa-clock-o"));
    $scope.analyticsOptions.push(new SidebarOptions("Purchases Per Session", "analytics.purchasesPerSession", ""));
    $scope.analyticsOptions.push(new SidebarOptions("Sessions First Purchase", "analytics.sessionsFirstPurchase", ""));
    $scope.analyticsOptions.push(new SidebarOptions("Sessions Bet. Purchases", "analytics.sessionsBetweenPurchase", ""));
      
    $scope.onDashboardClick = function(){
      $scope.showDashboardOptions = !$scope.showDashboardOptions;
      $scope.followLink('analytics.dashboard');
    };

    $scope.onAnalyticsClick = function(){
      $scope.showAnalyticsOptions = !$scope.showAnalyticsOptions;
    };

    $scope.onProductsClick = function(){
      $scope.followLink('analytics.products');
    };
      
    $scope.experimental = function(state){
      swal({
        title: "Are you sure?",
        text: "This feature is experimental by now",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes" },
        function(){
          $scope.followLink(state); //BUG: this function gets called correctly but swal call (l23) doesn't run.
        });
    }

  }]);
