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

application.controller('AppController', [
  '$scope',
  'cookiesManagerService',
  '$http',
  '$state',
  '$rootScope',
  'LoginLogoutService',
  'ItemSearchService',
  'ApplicationStateService',
  '$stateParams',
  'UserVoiceService',
  function (
    $scope,
    cookiesManagerService,
    $http,
    $state,
    $rootScope,
    LoginLogoutService,
    ItemSearchService,
    ApplicationStateService,
    $stateParams,
    UserVoiceService
  ) {

    UserVoiceService.bootstrap();

    $scope.logout = function(){
      LoginLogoutService.logout();
    };

    $scope.$on("LOGOUT_SUCCESS", function(event, url){
      //cleanup!
      $scope.applicationName = "";
      $scope.applicationsList = [];
      $scope.userInfo = {
        name: "",
        email: ""
      };

      ApplicationStateService.cleanup();
      $state.go("login");
    });

    //app related
    $scope.applicationsList = [];
    $scope.$on("APPLICATIONS_LIST_UPDATED", function() {
      $scope.applicationsList = ApplicationStateService.applicationsList;
    });

    $scope.chooseApplication = function(app){
      oldName = ApplicationStateService.getApplicationName();
      ApplicationStateService.updateApplicationName(app.name);
      _.each(app.platforms, function(platform){
        ApplicationStateService.addPlatforms(platform);
      });

      ApplicationStateService.updateCurrentApplication(app);
      
      mixpanel.register({"application": app});

      if($state.current.name === "analytics.dashboard" && oldName !== app.name){
        $state.transitionTo($state.current, $stateParams, {
            reload: true,
            inherit: false,
            notify: true
        });
      } else
        $state.go("analytics.dashboard");
    }

    //current page related
    $scope.$on("PAGE_UPDATED", function(){
      $scope.page = ApplicationStateService.getPath();
    });

    //user related
    user = ApplicationStateService.getUserInfo();
    $scope.userInfo = (user === null)? {name : "", email : ""} : user;

    $scope.$on("USER_INFO_UPDATED", function(){
        $scope.userInfo.name = ApplicationStateService.userInfo.name;
        $scope.userInfo.email = ApplicationStateService.userInfo.email;
    });

}]);
