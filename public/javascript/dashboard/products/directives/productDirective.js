dashboard.directive('product', ['$state', function($state) {
    return {
      restrict: 'E',
      scope: {
        model: '=info',
      },
      controller: function($scope) {
        $scope.switchDetailedView = function(state) {
          $state.go('analytics.productDetails', {productId: $scope.model.name});
        };
      },
      templateUrl: '/dashboard/product/directive'
    };
}]);
