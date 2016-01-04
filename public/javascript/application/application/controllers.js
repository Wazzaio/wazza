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

// application module

angular.module('ApplicationModule.controllers', ['ApplicationModule.services', 'angularFileUpload', 'DashboardModule']).
  controller('NewApplicationFormController', [
    '$scope',
    '$upload',
    'createNewApplicationService',
    '$route',
    '$state',
    'uploadAppImageService',
    'ApplicationStateService',
    function(
      $scope,
      $upload,
      createNewApplicationService,
      $route,
      $state,
      uploadAppImageService,
      ApplicationStateService
    ) {

    ApplicationStateService.setPath("Create New Application");
    ApplicationStateService.updateApplicationName("");

    $scope.noImageThumbnailUrl = "assets/images/placeholder_2.jpg";
    $scope.storeOptions = ['iOS', 'Android'];
    $scope.applicationForm = {
      "name": "",
      "url": "",
      "packageName": "",
      "imageName": $scope.noImageThumbnailUrl,
      "appType": []
    };

    $scope.formErrors = {};


    $scope.createApplication = function(formData){
      if($scope.applicationForm.appType.length == 0){
        swal("Oops...", "You must choose at least one Platform!", "error");
        return;
      }

      createNewApplicationService.send(formData)
        .then(
          function(result){
            $scope.formErrors = {};
            //invalidate previous list of applications and force new fetch
            ApplicationStateService.updateApplicationsList("");
            mixpanel.track("New application", {"name": formData.name});
            $state.go('analytics.overview');
            swal("New Application Created!", "Go to Overview to see them all.", "success")
          },
          function(errors){
            angular.extend($scope.formErrors, errors.data.errors);
          }
        );
    };

    $scope.imgThumb = "";
    $scope.AndroidSelected = false;
    $scope.iOSSelected = false;
    $scope.readyToSubmit = true;
    $scope.cssAndroidEnabled = "fa fa-android fa-2x store-selected";
    $scope.cssAndroidDisabled = "fa fa-android fa-2x store-unselected";
    $scope.cssiOSEnabled = "fa fa-apple fa-2x store-selected";
    $scope.cssiOSDisabled = "fa fa-apple fa-2x store-unselected";
    $scope.androidStoreCss = $scope.cssAndroidDisabled;
    $scope.iOSStoreCss = $scope.cssiOSDisabled;

    $scope.updatedStoreType = function(op, store) {
      if(op == 'add'){
        $scope.applicationForm.appType.push(store);
      } else {
        $scope.applicationForm.appType = _.without($scope.applicationForm.appType, store);
      }
    }

    $scope.$watch('AndroidSelected', function(newValue, oldValue, scope) {
      if(newValue) {
        $scope.androidStoreCss = $scope.cssAndroidEnabled;
        $scope.updatedStoreType('add', 'Android');
      } else {
        $scope.androidStoreCss = $scope.cssAndroidDisabled;
        $scope.updatedStoreType('remove', 'Android');
      }
    });

    $scope.$watch('iOSSelected', function(newValue, oldValue, scope) {
      if(newValue) {
        $scope.iOSStoreCss = $scope.cssiOSEnabled;
        $scope.updatedStoreType('add', 'iOS');
      } else {
        $scope.iOSStoreCss = $scope.cssiOSDisabled;
        $scope.updatedStoreType('remove', 'iOS');
      }
    }); 

    $scope.$watch(
      function(){
        return $scope.applicationForm;
      },

      function(newVal, oldVal){
        $scope.readyToSubmit = createNewApplicationService.validate($scope.applicationForm);
      },

      true
    );

    $scope.handlePhotoUploadSuccess = function(success) {
      $scope.applicationForm.imageName = success.data.url;
    };

    $scope.handlePhotoUploadError = function(error) {
      /** TODO **/
      console.log(error);
    }

    $scope.onFileSelect = function(files) {
      uploadAppImageService.execute(_.first(files))
        .then(
          $scope.handlePhotoUploadSuccess,
          $scope.handlePhotoUploadError
        );
    }

  }])
;
