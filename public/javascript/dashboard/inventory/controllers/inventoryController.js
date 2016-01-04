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
.controller('InventoryController', [
  '$scope',
  '$rootScope',
  'FetchItemsService',
  'BootstrapInventoryService',
  'DeleteItemService',
  'ApplicationStateService',
  'ItemSearchService',
  '$state',
  function (
    $scope,
    $rootScope,
    FetchItemsService,
    BootstrapInventoryService,
    DeleteItemService,
    ApplicationStateService,
    ItemSearchService,
    $state
    ) {

    ApplicationStateService.setPath("Management");

    $scope.addItem = function () {
        $state.go("home.newitem");
    };

    $scope.itemDeleteSucessCallback = function (data) {
        $scope.items = _.without($scope.items, _.findWhere($scope.items, {
            _id: data.data
        }));
    }

    /** TODO: show error message **/
    $scope.itemDeleteFailureCallback = function (data) {}

    $scope.deleteItem = function (id, image) {
        DeleteItemService(id, $scope.applicationName, image)
        .then(
            $scope.itemDeleteSucessCallback,
            $scope.itemDeleteFailureCallback
            );
    };

    $scope.successCallback = function (data) {
        var push = function (origin, destination) {
            _.each(origin, function (el) {
                destination.push(el);
            });
        };

        push(data.data.virtualCurrencies, $scope.virtualCurrencies);
        push(data.data.items, $scope.items);

    }

    $scope.failureCallback = function (errorData) {
        console.log(errorData);
    }

    $scope.virtualCurrencies = [];
    $scope.items = [];

    $scope.$on("ITEM_SEARCH_EVENT", function () {
        $scope.itemSearch = ItemSearchService.searchData
    });

    BootstrapInventoryService.execute()
    .then(
        $scope.successCallback,
        $scope.failureCallback);

}])
