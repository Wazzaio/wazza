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

dashboardServices.factory('DashboardCache', [
  'localStorageService',
  'ApplicationStateService',
  function(
    localStorageService,
    ApplicationStateService
  ) {

    function ChartCacheData(name, dates, data) {
      this.name = name;
      this.dates = dates;
      this.data = data;
    };

    var saveChartData = function(key, data) {
      // get service to fetch current dates
      var values = [];
      return localStorageService.set(key, new ChartCacheData(0, data));
    };
      
    var getChartData = function(name){
      var value = localStorageService.get(name);
      return (value === undefined) ? null : value.data;
    };

    var getTimeByKey = function(key) {
      var value = localStorageService.get(key);
      return (value === undefined) ? null : value.dates;
    };

    var deleteChartData = function(name) {
      return localStorageService.remove(name);
    };

    return {
      save: saveChartData,
      getChartData: getChartData,
      getDates: getTimeByKey,
      remove: deleteChartData
    };
}]);

