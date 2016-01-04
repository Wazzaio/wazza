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

dashboard.controller('SettingsController', [
  '$scope',
  '$location',
  '$rootScope',
  "$anchorScroll",
  "$state",
  "$document",
  'BootstrapSettingsService',
  'ApplicationStateService',
  'GetKPIService',
  "DateModel",
  "KpiModel",
  "$q",
  "CurrencyService",
  "CurrencyChanges",
  "UpdatePaymentCredentialsService",
  function (
    $scope,
    $location,
    $rootScope,
    $anchorScroll,
    $state,
    $document,
    BootstrapSettingsService,
    ApplicationStateService,
    GetKPIService,
    DateModel,
    KpiModel,
    $q,
    CurrencyService,
    CurrencyChanges,
    UpdatePaymentCredentialsService
  ) {

    var bootstrapSuccessCallback = function (data) {
      $scope.credentials = data.data.credentials;
      /** PayPal Credentials **/
      if(data.data.hasOwnProperty('payPalCredentials')) {
        $scope.payPalCredentials = data.data.payPalCredentials;
      }

      $scope.applications = ApplicationStateService.getApplicationsList();
      ApplicationStateService.setPath("Settings");
    };

    var bootstrapFailureCallback = function (errorData) {
      console.log(errorData);
    };

    $scope.appName = ApplicationStateService.applicationName;
      
    BootstrapSettingsService.execute()
      .then(
      bootstrapSuccessCallback,
      bootstrapFailureCallback
    );

    $scope.currencies = CurrencyService.getCurrencies();
    $scope.currentCurrency = ApplicationStateService.currency.name;
    $scope.changeCurrency = function(newCurrency) {
      $scope.currentCurrency = newCurrency;
      ApplicationStateService.changeCurrency(CurrencyService.getCurrency($scope.currentCurrency));
    };

    $scope.savePaymentChanges = function() {
      $scope.payPalCredentials.paymentSystem = 2;
      UpdatePaymentCredentialsService.execute(
        $scope.payPalCredentials,
        ApplicationStateService.applicationName,
        function(data) {
          swal("Success", "Changes were saved","success");
        },
        function(err) {
          swal("Error", "There was an error. Please try again", "error");
        }
      )
    };
  }]);

