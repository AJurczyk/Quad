var app = angular.module('quadServices', []);

app.factory('GyroChartSrv', function(){

    function GyroChartSrv(chartName) {
        var chartSpan = 100;
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
                  color: "#FF8A8A",
                  type: ['line'],
                  id: 'series' + chartName + 'raw'
                },
                {
                   axis: "y",
                   dataset: "dataSetClean",
                   key: "yValue",
                   label: chartName + "clean",
                   color: "#000000",
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
    };
    return GyroChartSrv;
});

app.factory('AngleChartSrv', function(){
    function AngleChartSrv(axisName) {
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

        this.addAngleReading = function(yValue){
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
                  label: "angle" + axisName,
                  color: "#FF0000",
                  type: ['line'],
                  id: 'series' + "Angle" + axisName
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

    return AngleChartSrv;
});

app.factory('GyroChartsMgr', ['GyroChartSrv', 'AngleChartSrv', function(GyroChartSrv, AngleChartSrv){
    var mgr={};
    mgr.xMin = 0;

    mgr.accX = new GyroChartSrv("AccX");
    mgr.accY = new GyroChartSrv("AccY");
    mgr.accZ = new GyroChartSrv("AccZ");
    mgr.gyroX = new GyroChartSrv("GyroX");
    mgr.gyroY = new GyroChartSrv("GyroY");
    mgr.gyroZ = new GyroChartSrv("GyroZ");

    mgr.angleX = new AngleChartSrv("X");
    mgr.angleY = new AngleChartSrv("Y");
    mgr.angleZ = new AngleChartSrv("Z");

    mgr.throttle = new AngleChartSrv("THROTTLE");

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
    mgr.addAngleReading = function(yValue){
        mgr.angleX.addAngleReading(yValue.angleX);
        mgr.angleY.addAngleReading(yValue.angleY);
        mgr.angleZ.addAngleReading(yValue.angleZ);
    };

    mgr.addThrottleReading = function(yValue){
        mgr.throttle.addAngleReading(yValue);
    };

    mgr.clear = function(){
        mgr.accX.clear();
        mgr.accY.clear();
        mgr.accZ.clear();

        mgr.gyroX.clear();
        mgr.gyroY.clear();
        mgr.gyroZ.clear();

        mgr.angleX.clear();
        mgr.angleY.clear();
        mgr.angleZ.clear();

        mgr.throttle.clear();
    };

    mgr.zoomInAll = function(){
        mgr.accX.zoomIn();
        mgr.accY.zoomIn();
        mgr.accZ.zoomIn();

        mgr.gyroX.zoomIn();
        mgr.gyroY.zoomIn();
        mgr.gyroZ.zoomIn();

        mgr.angleX.zoomIn();
        mgr.angleY.zoomIn();
        mgr.angleZ.zoomIn();

        mgr.throttle.zoomIn();
    };

    mgr.zoomOutAll = function(){
        mgr.accX.zoomOut();
        mgr.accY.zoomOut();
        mgr.accZ.zoomOut();

        mgr.gyroX.zoomOut();
        mgr.gyroY.zoomOut();
        mgr.gyroZ.zoomOut();

        mgr.angleX.zoomOut();
        mgr.angleY.zoomOut();
        mgr.angleZ.zoomOut();

        mgr.throttle.zoomOut();
    };

    mgr.setXmin = function(){
        mgr.accX.setXmin(mgr.xMin);
        mgr.accY.setXmin(mgr.xMin);
        mgr.accZ.setXmin(mgr.xMin);

        mgr.gyroX.setXmin(mgr.xMin);
        mgr.gyroY.setXmin(mgr.xMin);
        mgr.gyroZ.setXmin(mgr.xMin);

        mgr.angleX.setXmin(mgr.xMin);
        mgr.angleY.setXmin(mgr.xMin);
        mgr.angleZ.setXmin(mgr.xMin);

        mgr.throttle.setXmin(mgr.xMin);
    };
    return mgr;
}]);