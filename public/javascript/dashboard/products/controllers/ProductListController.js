function ProductInfo(name, price, revenue, contributionRate) {
  this.name = name;
  this.price = price;
  this.revenue = revenue;
  this.contributionRate = contributionRate;
};

dashboard.controller('ProductListController', [
  '$scope',
  'ApplicationStateService',
  function(
    $scope,
    ApplicationStateService
  ) {
    $scope.products = [];
    $scope.products.push(new ProductInfo("Hilton-Monaco", 20, 10234, 0.45));
    $scope.products.push(new ProductInfo("Ritz-Cannes", 30, 12000, 0.70));
    $scope.products.push(new ProductInfo("Marriot-NYC", 40, 15982, 0.68));      
    ApplicationStateService.setPath("Products");
      
  }]);

