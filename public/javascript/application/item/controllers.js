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

// item module

angular.module('ItemModule.controllers', ['ItemModule.services', 'angularFileUpload', 'DashboardModule']).
  controller('NewItemController',[
    '$scope',
    '$upload',
    'createNewItemService',
    '$routeParams',
    '$state',
    'getVirtualCurrenciesService',
    'uploadPhotoService',
    'ApplicationStateService',
    'GetLanguagesService',
    function (
      $scope,
      $upload,
      createNewItemService,
      $routeParams,
      $state,
      getVirtualCurrenciesService,
      uploadPhotoService,
      ApplicationStateService,
      GetLanguagesService
    ) {

      $scope.itemSearch = false;
      $scope.currencyOptions = ["Real","Virtual"];
      $scope.showCurrencyInputs = {
        "real": true,
        "virtual": false
      };

    $scope.bootstrapModule = function(){
      $scope.noImageThumbnailUrl = "assets/images/placeholder_2.jpg"
      $scope.itemForm = {
        "applicationName": ApplicationStateService.getApplicationName(),
        "name": "",
        "description": "",
        "store": 1,
        "metadata": {
          "osType": "",
          "title": "",
          "description": "",
          "publishedState": "published",
          "purchaseType": "managed_by_publisher",
          "autoTranslate": false,
          "locale": [],
          "autofill": false,
          "language": "", //TODO: get default lang
          "price": 0.0
        },
        "currency": {
          "typeOf": "Real",
          "value": 0.0,
          "virtualCurrency": "",
          "realCurrencies": ["Euro", "Dollar"]
        },
        "imageInfo": {
          "imageName": "",
          "url": $scope.noImageThumbnailUrl
        }
      };

      $scope.companyName = ApplicationStateService.getCompanyName();
      $scope.imgThumb = "";
      $scope.showCurrencyInputs.real = true;
      $scope.errors = false;
      $scope.formErrors = [];
      $scope.moneyCurrency = "$"; /*+ TODO: in the future get this via API **/
      $scope.currentCurrency = "$";
      $scope.virtualCurrencies = [];
      /*getVirtualCurrenciesService.execute($scope.itemForm.applicationName)
        .then(
          $scope.handleVirtualCurrencyRequestSuccess,
          $scope.handleVirtualCurrencyRequestError
        );*/
      $scope.itemForm.metadata.language = _.first(GetLanguagesService.languageOptions());
      $scope.$watch('itemForm.currency.typeOf', function(newValue, oldValue, scope){
        if (newValue == "Real") {
          $scope.showCurrencyInputs.real = true;
          $scope.showCurrencyInputs.virtual = false;
          $scope.itemForm.currency.virtualCurrency = "";
          $scope.currentCurrency = "$"
        } else {
          $scope.showCurrencyInputs.real = false;
          $scope.showCurrencyInputs.virtual = true;
        }
      });

      $scope.$watch('itemForm.currency.virtualCurrency', function(newValue, oldValue, scope) {
        if (newValue != "") {
          $scope.currentCurrency = newValue;
        }
      });
    };
    $scope.bootstrapModule();

    $scope.onFileSelect = function(files) {
      uploadPhotoService.execute(_.first(files))
        .then(
          $scope.handlePhotoUploadSuccess,
          $scope.handlePhotoUploadError
        );
    }

    $scope.handleVirtualCurrencyRequestSuccess = function(success){
      _.each(success.data, function(value, key, list){
        $scope.virtualCurrencies.push(value.name);
      });

      /**Removes virtual currency option if application has not virtual currencies **/
      if(_.size($scope.virtualCurrencies) == 0) {
        $scope.currencyOptions = _.filter($scope.currencyOptions, function(value){
          return value != "Virtual";
        });
      }
    };

    $scope.handleVirtualCurrencyRequestError = function(error){
      $scope.currencyOptions = _.filter($scope.currencyOptions, function(value){
          return value != "Virtual";
      });
    };

    $scope.handlePhotoUploadSuccess = function(success) {
      $scope.itemForm.imageInfo.url = success.data.url;
      $scope.itemForm.imageInfo.name = success.data.fileName;
    };

    $scope.handlePhotoUploadError = function(error) {
      /** TODO **/
      console.log(error);
    }

    $scope.handleSuccess = function(data){
      /*console.log(data);
      var hiddenElement = document.createElement('a');
      hiddenElement.href = 'data:attachment/csv,' + encodeURI(data.data);
      hiddenElement.target = '_blank';
      hiddenElement.download = 'myFile.csv';
      hiddenElement.click();*/
      $scope.errors = false;
      $scope.formErrors = [];
      $scope.itemSearch = true;
      $state.go("analytics.dashboard");
    }

    $scope.handleErrors = function(errors){
      $scope.errors = true;
      _.each(angular.fromJson(errors.data.errors), function(error){
        $scope.formErrors.push(error);
      });
    }

    $scope.createItem = function(){
      createNewItemService.send($scope.itemForm, $scope.myFile)
        .then(
          $scope.handleSuccess,
          $scope.handleErrors
        );
    };
  }])
;
