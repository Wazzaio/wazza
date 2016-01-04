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

var dashboard = angular.module('DashboardModule', [
    'ItemModule.services',
    'DashboardModule.services',
    'DashboardModule.controllers',
    'OverviewServices',
    'InventoryServices',
    'SettingsServices'
]);

dashboard.factory("KpiModel", [
  'HorizontalBarChartModel',
  'CurrencyService',
  'ApplicationStateService',
  function(
    HorizontalBarChartModel,
    CurrencyService,
    ApplicationStateService
  ) {
  function KpiModel(name, link) {
    this.isCollapsed = true;
    this.name = name;
    this.link = link;
    this.delta = 0;
    this.previous = 0;
    this.value = 0;
    this.unitType = "€";
    this.css = "kpi-delta";
    this.icon = "fa fa-minus";
    this.platforms = [];
    this.multiPlatform = true;
    this.chart = new HorizontalBarChartModel(1)
  };

  KpiModel.prototype = {
    currencyUpdate: function() {
      var _this = this;
      CurrencyService.getCurrencyExchange(ApplicationStateService.currency.globalID, function(res) {
        var rate = res.rates[Object.keys(res.rates)[0]];
        _this.value *= rate;
      });
    },
    updateChartData: function(chartData, platforms) {
      this.chart.updateChartData(chartData, platforms);
    },  
    updateKpiValue: function(data) {      
      var value = data.value;
      var delta = data.delta;
      var previous = data.previous;
      var DecimalPlaces = 2
      this.value = parseFloat(value.toFixed(DecimalPlaces));
      this.previous = parseFloat(previous.toFixed(DecimalPlaces));
      this.delta = parseFloat(delta.toFixed(DecimalPlaces));
      this.platforms = _.map(data.platforms, function(p) {
        p.value = parseFloat(p.value.toFixed(DecimalPlaces));
        p.previous = parseFloat(p.previous.toFixed(DecimalPlaces));
        p.delta = parseFloat(p.delta.toFixed(DecimalPlaces));
        return p;
      });
      this.updateChartData(data, _.map(data.platforms, function(p) {return p.platform;}));
    },
    updateUnitType: function(newType) {
      this.unitType = newType;
    },
    addPlatform: function(p) {
      if(!_.contains(this.platforms, p)) {
        this.platforms.push(p);
      }
    },
    removePlatform: function(p) {
      this.platforms = _.without(this.platforms, p);
    },
    
  };

  return KpiModel;
}]);

