'use strict';

dashboard
.controller('AnalyticsController', [
  '$scope',
  '$location',
  '$rootScope',
  'FetchItemsService',
  'BootstrapDashboardService',
  'DeleteItemService',
  'ApplicationStateService',
  'ItemSearchService',
  'TopbarService',
  function (
        $scope,
        $location,
        $rootScope,
        FetchItemsService,
        BootstrapDashboardService,
        DeleteItemService,
        ApplicationStateService,
        ItemSearchService,
        TopbarService
    ) {

        TopbarService.setName("Analytics");

        var dummyData = {
            "Users": {
                "totalMoneySpent": {
                    "UserLevel0": 0,
                    "UserLevel1": 0,
                    "UserLevel2": 20,
                    "UserLevel3": 10,
                    "UserLevel4": 10,
                    "UserLevel5": 40,
                    "UserLevel6": 67,
                    "UserLevel7": 80,
                    "UserLevel8": 80,
                    "UserLevel9": 90
                },
                "deviceInfo": {
                    "pieChart": {
                        "appVersion": {
                            "2": 10000,
                            "2.1": 45000,
                            "3": 4000000
                        },
                        "OS": {
                            "Android 2.3": 10000,
                            "Android 4.0": 20000,
                            "iOS 6": 4025000
                        },
                        "Screen Resolution": {
                            "240p": 5000,
                            "480p": 40000,
                            "720p": 2000000,
                            "1080p": 2000000
                        }
                    },
                    "map": {
                        "lat": "38",
                        "long": "34"
                    }
                },
                "moneySpentLines": {

                }

            },
            "boolea": true,
            "null": null,
            "number": 123,
            "object": {
                "a": "b",
                "c": "d",
                "e": "f"
            },
            "string": "Hello World"
        }


        var rows = [];

        for (var key in dummyData.Users.totalMoneySpent) {
            if (dummyData.Users.totalMoneySpent.hasOwnProperty(key)) {
                rows.push(dummyData.Users.totalMoneySpent[key]);
            }
        }


        $(function () {
            $('#revenue').highcharts({
                chart: {
                    type: 'column'
                },
                title: {
                    text: 'Revenue per User level'
                },
                xAxis: {
                    type: 'category',
                    labels: {
                        rotation: -45,
                        align: 'right',
                        style: {
                            fontSize: '13px',
                            fontFamily: 'Verdana, sans-serif'
                        }
                    }
                },
                legend: {
                    enabled: false
                },
                tooltip: {
                    pointFormat: "Total Money Spent in Q1 2014: <b>{point.y:.1f} millions</b>"
                },
                yAxis: {
                    title: {
                        text: 'in Million USD'
                    }
                },
                series: [{
                    name: 'Population',
                    data: rows,
                    dataLabels: {
                        enabled: true,
                        rotation: -90,
                        color: '#FFFFFF',
                        align: 'right',
                        x: 4,
                        y: 10,
                        style: {
                            fontSize: '13px',
                            fontFamily: 'Verdana, sans-serif',
                            textShadow: '0 0 3px black'
                        }
                    }
      }]
            });
        });

        $(function () {
            $('#users').highcharts({
                title: {
                    text: 'Users and Downloads evolution',
                    x: -20 //center
                },
                xAxis: {
                  type: 'datetime',
                  minRange: 7 * 24 * 3600000 // fourteen days
                    // categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                    // 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
                },
                yAxis: [{ // Primary yAxis
                    title: {
                        text: 'Users',
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    labels: {
                        format: '{value}',
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    }
            }, { // Secondary yAxis
                    title: {
                        text: 'Downloads',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    labels: {
                        format: '{value}',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    opposite: true
            }],
                tooltip: {
                    valueSuffix: ''
                },
                legend: {
                    layout: 'vertical',
                    align: 'right',
                    verticalAlign: 'middle',
                    borderWidth: 0
                },
                series: [{
                    name: 'Total Downloads',
                    type: 'spline',
                    pointInterval: 24 * 3600 * 7 *24,
                    pointStart: 1401289200,
                    data: [
                        0, 0, 0, 1, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0

                        ]
            }, {
                    name: 'Active Users',
                    type: 'spline',
                    pointInterval: 24 * 3600 * 7 *24,
                    pointStart: 1401289200,
                    data: [
                        0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0

                        ]
            }, {
                    name: 'Active Users with IAP',
                    type: 'spline',
                    pointInterval: 24 * 3600 * 7 *24,
                    pointStart: 1401289200,
                    data: [
                        0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0

                        ]
            }]
            });
        });

        $(function () {
            $('#activeUsersIAP').highcharts({
                chart: {
                    zoomType: 'xy'
                },
                title: {
                    text: 'Active users and IAPs'
                },
                xAxis: [{
type: 'datetime',
minRange: 7 * 24 * 3600000 // fourteen days
        }],
                yAxis: [{ // Primary yAxis
                    labels: {
                        format: '{value}',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    title: {
                        text: 'Active users with IAPs',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    opposite: true

        }, { // Secondary yAxis
                    gridLineWidth: 0,
                    title: {
                        text: 'IAP',
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    labels: {
                        format: '{value} ',
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    }

        }, { // Tertiary yAxis
                    gridLineWidth: 0,
                    title: {
                        text: 'Revenue',
                        style: {
                            color: Highcharts.getOptions().colors[2]
                        }
                    },
                    labels: {
                        format: '{value} USD',
                        style: {
                            color: Highcharts.getOptions().colors[2]
                        }
                    },
                    opposite: true
        }],
                tooltip: {
                    shared: true
                },
                legend: {
                    layout: 'vertical',
                    align: 'right',
                    verticalAlign: 'middle',
                    borderWidth: 0
                },
                series: [{
                    name: 'Active users with IAPs',
                    type: 'spline',
                    yAxis: 1,
                    data: [
                        0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0

                        ],
                    tooltip: {
                        valueSuffix: ' '
                    }

        }, {
                    name: 'IAPs/month',
                    type: 'spline',
                    yAxis: 2,
                    data: [
                        0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0

                        ],
                    marker: {
                        enabled: false
                    },
                    dashStyle: 'shortdot',
                    tooltip: {
                        valueSuffix: ' '
                    }

        }, {
                    name: 'Revenue/month',
                    type: 'spline',
                    data: [
                        0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0

                        ],
                    tooltip: {
                        valueSuffix: ' USD'
                    }
        }]
            });
        });

        $(function () {
            $('#appSession').highcharts({
                chart: {
                    zoomType: 'xy'
                },
                title: {
                    text: 'Session and IAPs'
                },
                xAxis: [{
                    categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
        }],
                yAxis: [{ // Primary yAxis
                    labels: {
                        format: '{value}',
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    title: {
                        text: 'Session Time',
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    opposite: true

        }, { // Secondary yAxis
                    gridLineWidth: 0,
                    title: {
                        text: 'Revenue / Session',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    labels: {
                        format: '{value} USD',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    }

        }],
                tooltip: {
                    shared: true
                },
                legend: {
                    layout: 'vertical',
                    align: 'right',
                    verticalAlign: 'middle',
                    borderWidth: 0
                },
                series: [{
                    name: 'Session time',
                    type: 'spline',
                    yAxis: 1,
                    data: [14, 13.8, 19, 29, 36.4, 43, 49.6, 0, 40.2, 28.2, 17.2, 10,
                    14, 19, 19, 29, 36.4, 0, 49.6, 48.2, 0, 28.2, 17.2, 10,
                    14, 13.8, 19, 29, 36.4, 14, 12.8, 19, 29, 40.4, 14, 15.8, 18, 29, 36.4],
                    tooltip: {
                        valueSuffix: ' '
                    }

        }, {
                    name: 'Revenue/session',
                    type: 'spline',
                    yAxis: 0,
                    data: [
                        0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0
                        , 0, 0, 0, 0, 0, 0, 0, 0

                        ],
                    marker: {
                        enabled: false
                    },
                    dashStyle: 'shortdot',
                    tooltip: {
                        valueSuffix: ' USD'
                    }

        }]
            });
        });



        $(function () {

            var colors = Highcharts.getOptions().colors,
                categories = ['Android', 'iOS'],
                name = 'Mobile Platforms',
                data = [{
                    y: 55.11,
                    color: colors[0],
                    drilldown: {
                        name: 'Android versions',
                        categories: ['Android 2.1', 'Android 2.2', 'Android 4', 'Android 4.2.2'],
                        data: [10.85, 7.35, 33.06, 2.81],
                        color: colors[0]
                    }
                }, {
                    y: 21.63,
                    color: colors[1],
                    drilldown: {
                        name: 'iOS versions',
                        categories: ['iOS 2.0', 'iOS 3.0', 'iOS 5', 'iOS 6', 'iOS 7.0'],
                        data: [0.20, 0.83, 1.58, 13.12, 5.43],
                        color: colors[1]
                    }
                }];


            // Build the data arrays
            var platformsData = [];
            var versionsData = [];
            for (var i = 0; i < data.length; i++) {

                // add browser data
                platformsData.push({
                    name: categories[i],
                    y: data[i].y,
                    color: data[i].color
                });

                // add version data
                for (var j = 0; j < data[i].drilldown.data.length; j++) {
                    var brightness = 0.2 - (j / data[i].drilldown.data.length) / 5;
                    versionsData.push({
                        name: data[i].drilldown.categories[j],
                        y: data[i].drilldown.data[j],
                        color: Highcharts.Color(data[i].color).brighten(brightness).get()
                    });
                }
            }

            // Create the chart
            $('#device').highcharts({
                chart: {
                    type: 'pie'
                },
                title: {
                    text: 'Device Info'
                },
                yAxis: {
                    title: {
                        text: 'DELETE Total percent market share'
                    }
                },
                plotOptions: {
                    pie: {
                        shadow: false,
                        center: ['50%', '50%']
                    }
                },
                tooltip: {
                    valueSuffix: '%'
                },
                series: [{
                    name: 'Platforms',
                    data: platformsData,
                    size: '60%',
                    datSaLabels: {
                        formatter: function () {
                            return this.y > 5 ? this.point.name : null;
                        },
                        color: 'white',
                        distance: -30
                    }
            }, {
                    name: 'Versions',
                    data: versionsData,
                    size: '80%',
                    innerSize: '60%',
                    dataLabels: {
                        formatter: function () {
                            // display only if larger than 1
                            return this.y > 1 ? '<b>' + this.point.name + ':</b> ' + this.y + '%' : null;
                        }
                    }
            }]
            });
        });

        //map
        /*
$(function () {

    $.getJSON('http://www.highcharts.com/samples/data/jsonp.php?filename=world-population-density.json&callback=?', function (data) {

        // Initiate the chart
        $('#map').highcharts('Map', {

            title : {
                text : 'Population density by country (/km²)'
            },

            mapNavigation: {
                enabled: true,
                buttonOptions: {
                    verticalAlign: 'bottom'
                }
            },

            colorAxis: {
                min: 1,
                max: 1000,
                type: 'logarithmic'
            },

            series : [{
                data : data,
                mapData: Highcharts.maps.world,
                joinBy: 'code',
                name: 'Population density',
                states: {
                    hover: {
                        color: '#BADA55'
                    }
                },
                tooltip: {
                    valueSuffix: '/km²'
                }
            }]
        });
    });
});
*/
}])
