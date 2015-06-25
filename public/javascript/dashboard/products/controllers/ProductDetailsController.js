dashboard.controller('ProductDetailsController', [
  '$scope',
  'LineChartModel',
  'ApplicationStateService',
  function($scope, LineChartModel, ApplicationStateService) {

    console.log("ProductDetailsController");
    ApplicationStateService.setPath("Product #1");

    $scope.revenueChart = new LineChartModel("Revenue");
    $scope.convertionChart = new LineChartModel("Convertion Rate");
    $scope.purchasesChart = new LineChartModel("Number of Purchases");
    $scope.usersChart = new LineChartModel("Unique Paying Users");

    var fetchChartData = function() {
      $scope.revenueChart.series.push("Revenue");
      $scope.revenueChart.labels = ["19 Jun", "20 Jun", "21 Jun", "22 Jun", "23 Jun", "24 Jun", "25 Jun"];
      $scope.revenueChart.data.push([0,0,1000,2500,3000,1500,2000]);
      $scope.revenueChart.colours.push({
        fillColor: 'rgba(79, 162, 216, 0.2)',
        strokeColor: 'rgba(79, 162, 216, 1.0)',
        pointColor: 'rgba(79, 162, 216, 1.0)',
        pointStrokeColor: '#fff',
        pointHighlightFill: '#fff',
        pointHighlightStroke: 'rgba(79, 162, 216, 0.8)'
      });

      $scope.convertionChart.series.push("Convertion Rate");
      $scope.convertionChart.labels = ["19 Jun", "20 Jun", "21 Jun", "22 Jun", "23 Jun", "24 Jun", "25 Jun"];
      $scope.convertionChart.data.push([0.20,0.10,0.45,0.56,0.80,0.45,0.38]);
      $scope.convertionChart.colours.push({
        fillColor: 'rgba(79, 162, 216, 0.2)',
        strokeColor: 'rgba(79, 162, 216, 1.0)',
        pointColor: 'rgba(79, 162, 216, 1.0)',
        pointStrokeColor: '#fff',
        pointHighlightFill: '#fff',
        pointHighlightStroke: 'rgba(79, 162, 216, 0.8)'
      });

      $scope.purchasesChart.series.push("Number of Purchases");
      $scope.purchasesChart.labels = ["19 Jun", "20 Jun", "21 Jun", "22 Jun", "23 Jun", "24 Jun", "25 Jun"];
      $scope.purchasesChart.data.push([50,100,25,25,50,100,100]);
      $scope.purchasesChart.colours.push({
        fillColor: 'rgba(79, 162, 216, 0.2)',
        strokeColor: 'rgba(79, 162, 216, 1.0)',
        pointColor: 'rgba(79, 162, 216, 1.0)',
        pointStrokeColor: '#fff',
        pointHighlightFill: '#fff',
        pointHighlightStroke: 'rgba(79, 162, 216, 0.8)'
      });

      $scope.usersChart.series.push("Unique Paying Users");
      $scope.usersChart.labels = ["19 Jun", "20 Jun", "21 Jun", "22 Jun", "23 Jun", "24 Jun", "25 Jun"];
      $scope.usersChart.data.push([10,20,70,50,26,100,50]);
      $scope.usersChart.colours.push({
        fillColor: 'rgba(79, 162, 216, 0.2)',
        strokeColor: 'rgba(79, 162, 216, 1.0)',
        pointColor: 'rgba(79, 162, 216, 1.0)',
        pointStrokeColor: '#fff',
        pointHighlightFill: '#fff',
        pointHighlightStroke: 'rgba(79, 162, 216, 0.8)'
      });
        
    };
    fetchChartData();

}]);

