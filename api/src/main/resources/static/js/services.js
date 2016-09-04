var app = angular.module('quadServices', []);

app.factory('GyroChartSrv', function(){
    var chartSpan = 100;

    function GyroChartSrv(chartName) {
        var chartName;
        var chartProbesCount = 0;
        this.data = {
            dataSetRaw: [],
            dataSetClean: []
            };

        this.options = {
              series: [
                {
                  axis: "y",
                  dataset: "dataSetRaw",
                  key: "yValue",
                  label: chartName + "raw",
                  color: "#FF0000",
                  type: ['line'],
                  id: 'series' + chartName + 'raw'
                },
                {
                   axis: "y",
                   dataset: "dataSetClean",
                   key: "yValue",
                   label: chartName + "clean",
                   color: "#00FF00",
                   type: ['line'],
                   id: 'series' + chartName + 'clean'
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

        this.clear = function(){
                chartProbesCount = 0;
                this.data.dataSetRaw = [];
                this.data.dataSetClean = [];
                this.options.axes.x.min = 0;
                this.options.axes.x.max = chartSpan;
        };

        this.addGyroRawReading = function(yValue){
            this.data.dataSetRaw.push({
                xValue: chartProbesCount,
                yValue: yValue
            });
        };

        this.addGyroCleanReading = function(yValue){
            this.data.dataSetClean.push({
                xValue: chartProbesCount,
                yValue: yValue
            });
            chartProbesCount++;
            if(chartProbesCount > chartSpan){
                this.options.axes.x.min++;
                this.options.axes.x.max++;
            }
        };
    };
    return GyroChartSrv;
});

app.factory('GyroChartsMgr', ['GyroChartSrv', function(GyroChartSrv){
    var mgr={};
    mgr.accX = new GyroChartSrv("AccX");
    mgr.accY = new GyroChartSrv("AccY");
    mgr.accZ = new GyroChartSrv("AccZ");
    mgr.gyroX = new GyroChartSrv("GyroX");
    mgr.gyroY = new GyroChartSrv("GyroY");
    mgr.gyroZ = new GyroChartSrv("GyroZ");

    mgr.addGyroRawReading = function(yValue){
        mgr.accX.addGyroRawReading(yValue.accX);
        mgr.accY.addGyroRawReading(yValue.accY);
        mgr.accZ.addGyroRawReading(yValue.accZ);
        mgr.gyroX.addGyroRawReading(yValue.gyroX);
        mgr.gyroY.addGyroRawReading(yValue.gyroY);
        mgr.gyroZ.addGyroRawReading(yValue.gyroZ);
    };
    mgr.addGyroCleanReading = function(yValue){
        mgr.accX.addGyroCleanReading(yValue.accX);
        mgr.accY.addGyroCleanReading(yValue.accY);
        mgr.accZ.addGyroCleanReading(yValue.accZ);
        mgr.gyroX.addGyroCleanReading(yValue.gyroX);
        mgr.gyroY.addGyroCleanReading(yValue.gyroY);
        mgr.gyroZ.addGyroCleanReading(yValue.gyroZ);
    };
    mgr.clear = function(){
        mgr.accX.clear();
        mgr.accY.clear();
        mgr.accZ.clear();
        mgr.gyroX.clear();
        mgr.gyroY.clear();
        mgr.gyroZ.clear();
    };
    return mgr;
}]);