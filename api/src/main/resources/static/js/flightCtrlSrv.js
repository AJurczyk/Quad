var app = angular.module('flightCtrlServices', []);

app.factory('Entry', function($resource) {
    return $resource('/getFlightEvents',[], {
        getFlightEvents: {
            method: 'get',
            isArray: true
        }
    });
});

app.factory('flightCtrlSrv',['$resource', function($resource){
    return $resource('/startStopFlightCtrl:state', {},
    {
        startStop: {
            url: '/startStopFlightCtrl?run=:state'
        },
        setDesiredAngle: {
            url: "/setDesiredAngle?angle=:state"
        },
        setCurrentAngle: {
            url: "/setCurrentAngle?angle=:state"
        },
        setP: {
            url: "/setP?value=:state"
        },
        setI: {
            url: "/setI?value=:state"
        },
        setD: {
            url: "/setD?value=:state"
        }
    });
}]);

app.factory('SingleChartSrv', function(){
    function SingleChartSrv(axisName) {
        var chartSpan = 100;
        var chartProbesCount = 0;
        this.data = {
            dataSet: []
        };

        this.clear = function(){
            chartProbesCount = 0;
            this.data.dataSet = [];
            this.options.axes.x.min = 0;
            this.options.axes.x.max = chartSpan;
        };

        this.addReading = function(yValue){
            this.data.dataSet.push({
                xValue: chartProbesCount,
                yValue: yValue
            });
            chartProbesCount++;
            if(chartProbesCount > chartSpan){
                this.options.axes.x.min++;
                this.options.axes.x.max++;
            }
        };

        this.zoomIn = function() {
            var step = 30;
            if(chartSpan > step){
                chartSpan = chartSpan - step;
                this.options.axes.x.max=this.options.axes.x.max - step;
            }
        };

        this.zoomOut = function() {
            var step = 30;
            chartSpan = chartSpan + step;
            this.options.axes.x.max = this.options.axes.x.max + step;
        };

        this.setXmin = function(xMin) {
            this.options.axes.x.min = xMin;
            this.options.axes.x.max = xMin + chartSpan;
        };

        this.options = {
              series: [
                {
                  axis: "y",
                  dataset: "dataSet",
                  key: "yValue",
                  label: axisName,
                  color: "#FF0000",
                  type: ['line'],
                  id: 'series' + axisName
                }
              ],
              axes: {
                x: {
                    key: "xValue",
                    min: 0,
                    max: chartSpan,
                }
              },
              pan: {
                    x: true,
                    y: false
              }
            };
    };

    return SingleChartSrv;
});