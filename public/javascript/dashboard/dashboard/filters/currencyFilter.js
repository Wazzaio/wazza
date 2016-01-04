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

dashboard.filter('wzCurrencyFilter', [
  'ApplicationStateService',
  'CurrencyService',
  function(
    ApplicationStateService,
    CurrencyService
  ) {
  return function(input, kpiName) {
    var out = "";
    var kpis = [
      'Total Revenue', 'Avg Revenue Per User', 'Avg Revenue per Session',
      'Average Revenue Per User', 'Average Revenue Per Session', 'Life Time Value'
    ];
    if(_.contains(kpis, kpiName)) {
      switch(ApplicationStateService.currency.name) {
        case 'Euro':
            out = CurrencyService.getCurrency('Euro').symbol + " " + input;
          break;
        case 'Dollar':
            out = CurrencyService.getCurrency('Dollar').symbol + " " + input;
          break;
      }
    } else {
      out = input;
    }
      
    return out;
  };
}]);

