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

dashboard.controller('ProductListController', [
  '$scope',
  'ApplicationStateService',
  'ProductInfo',
  function(
    $scope,
    ApplicationStateService,
    ProductInfo
  ) {
    $scope.products = [];
    $scope.products.push(new ProductInfo("Hilton-Monaco", 20, 10234, 0.45));
    $scope.products.push(new ProductInfo("Ritz-Cannes", 30, 12000, 0.70));
    $scope.products.push(new ProductInfo("Marriot-NYC", 40, 15982, 0.68));      
    ApplicationStateService.setPath("Products");
      
  }]);

