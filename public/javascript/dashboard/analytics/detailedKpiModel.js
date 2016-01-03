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

dashboard.factory('DetailedKpiModel', ['KpiModel', 'LineChartModel', function(KpiModel, LineChartModel) {
     
  function DetailedKpiModel(beginDate, endDate, name) {
    this.beginDate = beginDate;
    this.endDate = endDate;
    this.chart = new LineChartModel(name);
    this.model = new KpiModel(name, "");
  };

  DetailedKpiModel.prototype = {
    updateDates: function(begin, end) {
      this.beginDate = begin;
      this.endDate = end;
    },
    updateChartData: function(chartData, platforms) {
      this.chart.updateChartData(chartData, platforms);
    },
    removeSerieFromChart: function(serieKey) {
      this.chart.removeSeries(serieKey);
    }
  };
    
  return DetailedKpiModel;
}]);
