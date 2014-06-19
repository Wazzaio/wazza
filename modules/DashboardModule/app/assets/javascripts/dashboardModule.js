'use strict';

var dashboard = angular.module('DashboardModule', ['ItemModule.services'])

.controller('DashboardController', [
  '$scope',
  '$location',
  '$rootScope',
  'FetchItemsService',
  'BootstrapDashboardService',
  'DeleteItemService',
  'ApplicationStateService',
  'ItemSearchService',
  'TopbarService',
  function (
        $scope,
        $location,
        $rootScope,
        FetchItemsService,
        BootstrapDashboardService,
        DeleteItemService,
        ApplicationStateService,
        ItemSearchService,
        TopbarService
    ) {

        $scope.bootstrapSuccessCallback = function (data) {
            var push = function (origin, destination) {
                _.each(origin, function (el) {
                    destination.push(el);
                });
            };

            angular.extend($scope.credentials, data.data.credentials);
            push(data.data.virtualCurrencies, $scope.virtualCurrencies);
            push(data.data.items, $scope.items);
            push(
                _.map(data.data.applications, function (element) {
                    return element.name;
                }),
                $scope.applications
            );
            ApplicationStateService.updateApplicationName(_.first(data.data.applications).name);
            ApplicationStateService.updateUserInfo(data.data.userInfo);

            TopbarService.setName("Dashboard");

            $(function () {
                $('#revenueDashboard').highcharts({
                    chart: {
                        zoomType: 'x'
                    },
                    title: {
                        useHTML: true,
                        text: '<span class="pull-right">Sales per time<i class="fa fa-android fa-fw"></i></span>'
                    },
                    subtitle: {
                        text: document.ontouchstart === undefined ?
                            'Click and drag in the plot area to zoom in' : 'Pinch the chart to zoom in'
                    },
                    xAxis: {
                        type: 'datetime',
                        minRange: 7 * 24 * 3600000 // fourteen days

                    },
                    yAxis: {
                        title: {
                            text: 'Revenue (in USD)'
                        },
                        min: 0
                    },
                     legend: {
                        layout: 'vertical',
                        align: 'right',
                        verticalAlign: 'middle',
                        borderWidth: 0
                    },
                    series: [{
                        type: 'line',
                        name: '<span class="pull-right">Android Revenue<i class="fa fa-android fa-fw"></i></span>',
                        pointInterval: 24 * 3600 * 1000,
                        pointStart: 1328004400000,
                        data: [
                            0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                            , 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0
                            , 0, 0, 0, 0, 0, 0, 0, 0

                            ]
                    }, {
                        type: 'line',
                        name: '<span class="pull-right">Apple Revenue<i class="fa fa-apple fa-fw"></i></span>',

                        pointInterval: 24 * 3600 * 1000,
                        pointStart: 1328004400000,
                        data: [
                            0.734, 0.7336, 0.7351, 0.7346, 0.7321, 0.7294, 0.7266, 0.7266, 0.7254, 0.7242,
                            0.7213, 0.7197, 0.7209, 0.721, 0.721, 0.721, 0.7209, 0.7159, 0.7133, 0.7105,
                            0.6818, 0.6836, 0.6823, 0.6805, 0.6793, 0.6849, 0.6833, 0.6825, 0.6825, 0.6816,
                            0.6799, 0.6813, 0.6809, 0.6868, 0.6933, 0.6933, 0.6945, 0.6944, 0.6946, 0.6964,
                            0.714, 0.7119, 0.7129, 0.7129, 0.7049, 0.7095
                        ]
                    }]

                });
            });

        }

        $scope.bootstrapFailureCallback = function (errorData) {
            console.log(errorData);
        }

        $scope.bootstrapModule = function () {
            $scope.applicationName = "";
            $scope.applications = [];
            $scope.credentials = {};
            $scope.virtualCurrencies = [];
            $scope.items = [];
            $scope.isCollapsed = true;
            $scope.$on("ITEM_SEARCH_EVENT", function () {
                $scope.itemSearch = ItemSearchService.searchData
            });
            $scope.$on("APPLICATION_NAME_UPDATED", function () {
                $scope.applicationName = ApplicationStateService.applicationName;
            });
            BootstrapDashboardService.execute()
                .then(
                    $scope.bootstrapSuccessCallback,
                    $scope.bootstrapFailureCallback);
        };
        $scope.bootstrapModule();

    }])

.factory('BootstrapDashboardService', ['$http', '$q',
    function ($http, $q) {
        var service = {};

        service.execute = function () {
            var request = $http({
                url: '/dashboard/bootstrap',
                method: 'GET'
            });

            var deferred = $q.defer();
            deferred.resolve(request);
            return deferred.promise;
        };

        return service;
}])

.factory('ApplicationStateService', ['$rootScope',
    function ($rootScope) {
        var service = {};
        service.applicationName = "";
        service.applicationsList = [];
        service.userInfo = {
            name: "",
            email: ""
        };

        service.updateApplicationName = function (newName) {
            service.applicationName = newName;
            $rootScope.$broadcast("APPLICATION_NAME_UPDATED");
        };

        service.updateApplicationsList = function (newList) {
            service.appplicationsList = newList;
            $rootScope.$broadcast("APPLICATIONS_LIST_UPDATED");
        };

        service.updateUserInfo = function (newInfo) {
            service.userInfo = newInfo;
            $rootScope.$broadcast("USER_INFO_UPDATED");
        };

        return service;
}])

.factory('FetchItemsService', ['$http', '$q',
    function ($http, $q) {
        var service = {};

        service.execute = function (appName, offset) {
            var request = $http({
                url: '/app/api/item/get/' + appName + '/' + offset,
                method: 'GET'
            });

            var deferred = $q.defer();
            deferred.resolve(request);
            return deferred.promise;
        };

        return service;
}])

.factory('DeleteItemService', ['$http', '$q',
    function ($http, $q) {
        var service = function (id, name, imageName) {
            var request = $http.post("/app/item/delete/" + id, {
                appName: name,
                image: imageName
            });

            var deferred = $q.defer();
            deferred.resolve(request);
            return deferred.promise;
        };

        return service;
}])

;
