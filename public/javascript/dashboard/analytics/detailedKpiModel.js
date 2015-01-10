'use strict';

dashboard.factory('DetailedKpiModel', ['KpiModel', 'LineChartModel', function(KpiModel, LineChartModel) {
     
  function DetailedKpiModel(beginDate, endDate, name) {
    this.beginDate = beginDate;
    this.endDate = endDate;
    this.chart = new LineChartModel(0);
    this.model = new KpiModel(name, "");
  };

  DetailedKpiModel.prototype = {
    updateDates: function(begin, end) {
      this.beginDate = begin;
      this.endDate = end;
    },
    updateChartData: function(chartData, platforms) {
      this.chart.updateChartData(chartData, platforms);
    }
  };
    
  return DetailedKpiModel;
}]);
