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

inventoryServices.factory('FetchItemsService', ['$http', '$q',
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
}]);
