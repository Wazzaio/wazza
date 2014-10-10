
var application = angular.module('Wazza.controllers', [
  'ApplicationModule',
  'Wazza.services',
  'ItemModule',
  'ngCookies',
  'SecurityModule',
  'DashboardModule',
  'ui.bootstrap'
  ])

.controller('NavBarController',[
  '$scope',
  'LoginLogoutService',
  'DateModel',
  '$state',
  '$rootScope',
  function (
    $scope,
    LoginLogoutService,
    DateModel,
    $state,
    $rootScope
    ) {

    $scope.logout = function(){
      LoginLogoutService.logout();
    };

    $scope.today = function() {
      DateModel.initDateInterval();
      $scope.beginDate = DateModel.startDate;
      $scope.endDate = DateModel.endDate;
    };

    $scope.toggleMin = function() {
      $scope.minDate = moment().subtract(1, 'years').format('d-M-YYYY');
      $scope.endDateMin = $scope.beginDate;
    };

    $scope.updateEndDateMin = function(){
      $scope.endDateMin = $scope.beginDate;
    };

    $scope.openBeginDate = function($event) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope.beginDateOpened = true;
    };

    $scope.openEndDate = function($event) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope.endDateOpened = true;
    };

    $scope.format = 'dd-MMM-yyyy';
    $scope.today();
    $scope.toggleMin();
    $scope.maxDate = new Date();
    $scope.initDate = $scope.today;

    $scope.updateKPIs = function() {
      DateModel.startDate = $scope.beginDate;
      DateModel.endDate = $scope.endDate;
      $rootScope.$broadcast($state.current.name);
    }
  }])

.controller('SidebarController', [
  '$scope',
  '$rootScope',
  '$state',
  'ApplicationStateService',
  function(
    $scope,
    $rootScope,
    $state,
    ApplicationStateService
    ) {
    
    $scope.selectDashboardSection = function(sectionId) {
      $scope.followLink("analytics.dashboard");
      $rootScope.$broadcast('ChangeDashboardSection', {section: sectionId});
    };

    $scope.followLink = function(state){
      if(ApplicationStateService.applicationName === ""){
        swal("Which Application?", "You should definitively choose one first!")
      }
      else
        $state.go(state);
    }

    $scope.experimental = function(state){
      swal({
        title: "Are you sure?",
        text: "This feature is experimental by now",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Yes, I have no fear!" },
        function(){
          $state.go(state);
        });
    }

  }])

.controller('NotAvailableYetController', [
  '$scope',
  '$rootScope',
  'ApplicationStateService',
  function(
    $scope,
    $rootScope,
    ApplicationStateService
    ) {
    ApplicationStateService.setPath("Not available yet :(");

  }])

;
