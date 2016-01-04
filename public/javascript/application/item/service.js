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

angular.module('ItemModule.services', ['DashboardModule']).
  factory('createNewItemService', [
    '$upload',
    '$q',
    'ApplicationStateService',
    function (
      $upload,
      $q,
      ApplicationStateService
    ) {
    var service = {};

    service.send = function(formData, file){
      var requestUrl = '/app/item/new/' + ApplicationStateService.getCompanyName() + '/' + formData.applicationName
      var request = $upload.upload({
          url: requestUrl,
          method: 'POST',
          data: formData,
          file: file
      });

      var deferred = $q.defer();
      deferred.resolve(request);
      return deferred.promise;
    };

    return service;
  }]).

  factory('uploadPhotoService', ['$upload', '$q', function ($upload, $q) {
    var service = {};

    service.execute = function(file){
      var request = $upload.upload({
        url: '/app/item/uploadimage',
        method: 'POST',
        file: file
      });

      var deferred = $q.defer();
      deferred.resolve(request);
      return deferred.promise;
    };

    return service;
  }]).

  factory('getVirtualCurrenciesService', [
    '$http',
    '$q',
    'ApplicationStateService',
    function (
      $http,
      $q,
      ApplicationStateService
    ) {
    var service = {};

    service.execute = function(applicationName){
      var baseUrl = '/app/api/virtualcurrencies/all/';
      var requestUrl = baseUrl + applicationName + '/' + ApplicationStateService.getCompanyName();
      var request = $http({
        url: requestUrl,
        method: 'GET'
      });

      var deferred = $q.defer();
      deferred.resolve(request);
      return deferred.promise;
    };

    return service;
  }]).

  factory('ItemSearchService', ['$rootScope', function ($rootScope) {
    var service = {};  
    service.searchData = "";

    service.updateSearchData = function(newData){
      service.searchData = newData;
      $rootScope.$broadcast("ITEM_SEARCH_EVENT");
    };

    return service;
  }]).

  factory('GetLanguagesService', [function () {
    var service = {};
    service.languages = {
      "Portuguese": "pt_PT",
      "Chinese":    "zh_TW",
      "Italian":    "it_IT",
      "Czech":      "cs_CZ",
      "Japanese":   "ja_JP",
      "Danish":     "da_DK",
      "Korean":     "ko_KR",
      "Dutch":      "nl_NL",
      "Norwegian":  "no_NO",
      "English":    "en_US",
      "Polish":     "pl_PL",
      "French":     "fr_FR",
      "Finnish":    "fi_FI",
      "Russian":    "ru_RU",
      "German":     "de_DE",
      "Spanish":    "es_ES",
      "Hebrew":     "iw_IL",
      "Swedish":    "sv_SE",
      "Hindi":      "hi_IN"
    };

    service.languageOptions = function(){
      return _.map(service.languages, function(value, key){
        return key;
      });
    };

    return service;
  }])
;
