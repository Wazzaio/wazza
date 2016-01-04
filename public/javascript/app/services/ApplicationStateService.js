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

service.factory('ApplicationStateService', [
  '$rootScope',
  'localStorageService',
  'SelectedPlatformsChange',
  'CurrencyService',
  'CurrencyChanges',
  'CurrentAppChanges',
	function (
    $rootScope,
    localStorageService,
    SelectedPlatformsChange,
    CurrencyService,
    CurrencyChanges,
    CurrentAppChanges
  ) {
      
    function AppInfo(name, platforms, paymentSystems) {
      this.name = name;
      this.platforms = platforms;
      this.paymentSystems = paymentSystems;
    };

    this.currentApplication = {};
	this.applicationName = "";
	this.companyName = "";
	this.applicationsList = [];
    this.userInfo = {
		name: "",
		email: ""
	};
	this.path = "";
	this.applicationOverview = "";
    this.selectedPlatforms = [];
    this.apps = [];
    this.currency = CurrencyService.getDefaultCurrency();

		//current view
		this.getPath = function () {
			return this.path;
		};

		this.setPath = function(value) {
			this.path = value;
			$rootScope.page = value;
			$rootScope.$broadcast("PAGE_UPDATED");
		};

		//current selected app
		this.getApplicationName = function () {
			return this.applicationName;
		}

		this.updateApplicationName = function (newName) {
			this.applicationName = newName;
			$rootScope.applicationName = newName;
			$rootScope.$broadcast("APPLICATION_NAME_UPDATED"); 
		};

	    this.updateApps = function(apps) {
	      var _apps = [];
	      _.each(apps, function(appInfo) {
	        _apps.push(new AppInfo(appInfo.name, appInfo.platforms, appInfo.paymentSystems));
	      });
	      this.apps = _apps;
	    }

		//currently logged company
		this.getCompanyName = function(newName) {
			return this.companyName;
		};

		this.updateCompanyName = function(newName) {
			this.companyName = newName;
			$rootScope.$broadcast("COMPANY_NAME_UPDATED");
		};

		//applications of logged user
		this.getApplicationsList = function (newList) {
			return this.applicationsList;
		};

		this.updateApplicationsList = function (newList) {
			this.applicationsList = newList.slice(0);
			$rootScope.$broadcast("APPLICATIONS_LIST_UPDATED");
		};

		//applications information (overview view)
		this.getApplicationsOverview = function () {
			return this.applicationOverview;
		}

		this.updateApplicationsOverview = function (apps) {
			this.applicationOverview = apps;
		};

		//user info: name & mail
		this.getUserInfo = function () {
			this.userInfo = localStorageService.get("userInfo");
			return this.userInfo;
		};

		this.updateUserInfo = function (newInfo) {
			this.userInfo = newInfo;
			localStorageService.set("userInfo", newInfo);
			$rootScope.$broadcast("USER_INFO_UPDATED");
		};

		//hack to mantain initial clean state
		this.cleanup = function () {
			this.applicationName = "";
			this.companyName = "";
			this.applicationsList = [];
			this.userInfo = {
				name: "",
				email: ""
			};
			this.path = "";
			this.applicationOverview = "";
		};

    // Platform operations: add and remove platforms that are presented
    this.resetPlatforms = function() {
      this.selectedPlatforms = [];
    };

    this.addPlatforms = function(platform) {
      if(!_.contains(this.selectedPlatforms, platform)) {
        this.selectedPlatforms.push(platform);
      }
      $rootScope.$broadcast(SelectedPlatformsChange);
    };

    this.removePlatform = function(platform) {
      this.selectedPlatforms = _.without(this.selectedPlatforms, platform);
      $rootScope.$broadcast(SelectedPlatformsChange);
    };

    this.changeCurrency = function(newCurrency) {
      this.currency = newCurrency;
      $rootScope.$broadcast(CurrencyChanges);
    };

    this.updateCurrentApplication = function(app) {
      this.currentApplication = app;
      $rootScope.$broadcast(CurrentAppChanges);
    };
      
		return this;
	}
]);

