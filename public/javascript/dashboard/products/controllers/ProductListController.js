dashboard.controller('ProductListController', [
  '$scope',
  'ApplicationStateService',
  function(
    $scope,
    ApplicationStateService
  ) {

    ApplicationStateService.setPath("Products")
  }]);

