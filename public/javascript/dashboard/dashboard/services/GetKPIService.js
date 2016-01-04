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

dashboardServices.factory('GetKPIService', ['$http', '$q',
    function($http, $q) {
      var buildUrl = function(companyName, applicationName, urlType, subType, startDate, endDate) {
        var url = ('/analytics/' +
         urlType + '/' +
         subType + '/' +
         companyName + '/' +
         applicationName + '/'+
         startDate + '/' +
         endDate).replace(" ", "%20");
        return url;
      };

      this.getTotalKpiData = function(companyName, applicationName, start, end, kpiName, platforms, paymentSystems) {
        var request = $http({
          url: buildUrl(companyName, applicationName, kpiName, "total", start, end),
          method: 'GET',
          headers: {
            "X-Platforms": platforms,
            "X-PaymentSystems": paymentSystems
          }
        });

        var deferred = $q.defer();
        deferred.resolve(request);
        return deferred.promise;
      };

      this.getDetailedKPIData = function(companyName, applicationName, start, end, kpiName, platforms, paymentSystems) {
        var request = $http({
          url: buildUrl(companyName, applicationName, kpiName, "detail", start, end),
          method: 'GET',
          headers: {
            "X-Platforms": platforms,
            "X-PaymentSystems": paymentSystems
          }
        });

        var deferred = $q.defer();
        deferred.resolve(request);
        return deferred.promise;
      };

      return this;
}])
