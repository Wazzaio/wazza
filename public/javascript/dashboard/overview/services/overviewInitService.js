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

overviewServices.factory('OverviewInitService', ['$http', '$q',
  function($http, $q) {

    var service = {};

    service.getCompany = function(){
      var request = $http.get("/dashboard/overview/company");
      var deferred = $q.defer();
      deferred.resolve(request);
      return deferred.promise;
    }

    service.getApplications = function() {
      var deferred = $q.defer();
      deferred.resolve($http({
        url: '/dashboard/overview/bootstrap',
        method: 'GET'
      }));
      return deferred.promise;
    };

    return service;
}]);
