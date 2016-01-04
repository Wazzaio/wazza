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

service.factory('CurrencyService', ['$rootScope', '$resource', function($rootScope, $resource) {
  function CurrencyInfo(name, globalID, cssSymbol, symbol) {
    this.name = name;
    this.globalID = globalID;
    this.cssSymbol = cssSymbol;
    this.symbol = symbol;
  }

  var currencies = [];
  currencies.push(new CurrencyInfo("Euro", "EUR", "fa fa-eur", "€"));
  currencies.push(new CurrencyInfo("Dollar", "USD", "fa fa-usd", "$"));

  this.getCurrency = function(name) {
    return _.find(currencies, function(c) {return c.name == name;});
  };

  this.getCurrencies = function() {return currencies;}

  this.getCurrencyExchange = function(_id, successCallback) {
    var Currency = $resource("http://api.fixer.io/latest?symbols=:currencyID");
    Currency.get({currencyID: _id}, function(res) {
      successCallback(res);
    });
  };

  this.getDefaultCurrency = function() {
    return this.getCurrency("Euro");
  };

  return this;
}]);

