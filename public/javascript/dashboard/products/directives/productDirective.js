dashboard.directive('product', ['$state', function($state) {
    return {
      restrict: 'E',
      scope: {
        model: '=info',
      },
      controller: function($scope) {
        $scope.switchDetailedView = function(state) {
          $state.go(state);
        };
      },
      templateUrl: '/dashboard/product/directive'
    };
}]);
