angular.module('Wazza.services', []).

  factory('submitLoginCredentialsService', ['$http', '$q', function($http, $q) {
    var service = {};

    service.execute = function(loginData){
      var request = $http.post("/login", loginData);
      var deferred = $q.defer();
      deferred.resolve(request);
      return deferred.promise;
    };
    return service;
  }]).

  factory('redirectToDashboardService', ['$http', '$q', function($http, $q) {
    var service = {};

    service.execute = function() {
      var request = $http.get("/dashboard");
      var deferred = $q.defer();
      deferred.resolve(request);
      return deferred.promise;
    };

    return service;
  }
  ]).

  factory('LoginLogoutService', ['$rootScope', '$http', function ($rootScope, $http) {
    var service = {};

    service.login = function(){
      $rootScope.$broadcast("LOGIN_SUCCESS");
    };

    service.logout = function(logoutData){

      var handleLogoutSuccess = function(logoutData){
        $rootScope.$broadcast("LOGOUT_SUCCESS", {value: logoutData.data});
      };

      var handleLogoutFailure = function(data){
        $rootScope.$broadcast("LOGOUT_ERROR", {value: data});
      };

      $http.post("/logout")
      .then(handleLogoutSuccess, handleLogoutFailure);
    };
  
    return service;
  }]).

  factory('TopbarService', ['$rootScope', function ($rootScope) {
        var service = {};
        service.pagename = '';

        service.getName = function () {
                return pagename;
        };

        service.setName = function(value) {
                pagename = value;
                $rootScope.$broadcast("PAGE_UPDATED");
        };

        return service;
    }]).


  factory('ApplicationStateService', ['$rootScope',
      function ($rootScope) {
          var service = {};
          service.applicationName = "";
          service.companyName = "";
          service.applicationsList = [];
          service.userInfo = {
              name: "",
              email: ""
          };

          service.updateApplicationName = function (newName) {
              service.applicationName = newName;
              $rootScope.$broadcast("APPLICATION_NAME_UPDATED");
          };

          service.updateCompanyName = function(newName) {
              service.companyName = newName;
              $rootScope.$broadcast("COMPANY_NAME_UPDATED");
          };

          service.updateApplicationsList = function (newList) {
              service.applicationsList = newList.slice(0);
              $rootScope.$broadcast("APPLICATIONS_LIST_UPDATED");
          };

          service.updateUserInfo = function (newInfo) {
              service.userInfo = newInfo;
              $rootScope.$broadcast("USER_INFO_UPDATED");
          };

          return service;
  }]).

    factory('DateModel', function() {
      var model = function() {
        this.startDate = new Date();
        this.endDate = new Date();
      };

      model.initDateInterval = function() {
        this.startDate= new Date(moment().subtract('days', 7));
        this.endDate = new Date();
      };

      model.formatDate = function(date) {
        return moment(date).format('DD-MM-YYYY');
      };
        
      return model;
    })
;

