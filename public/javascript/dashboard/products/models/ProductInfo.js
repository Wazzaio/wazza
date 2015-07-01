'use strict';

dashboard.factory('ProductInfo', function() {
  function ProductInfo(name, price, revenue, contributionRate) {
    this.name = name;
    this.price = price;
    this.revenue = revenue;
    this.contributionRate = contributionRate;
  };

  return ProductInfo;
    
});

