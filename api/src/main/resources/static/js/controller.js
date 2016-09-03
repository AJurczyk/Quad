var app = angular.module('myApp', ['ngResource', 'n3-line-chart', 'emguo.poller']);

app.filter('reverse', function() {
  return function(items) {
    return items.slice().reverse();
  };
});

app.factory('Entry', function($resource) {
    return $resource('/getEvents',[], {
        myQuery: {
            method: 'get',
            isArray: true
        }
    });
});

app.factory('StartStopSrv',['$resource', function($resource){
    return $resource('/startStopGyro:state', {},
        {
            startOrStop: {
                url: '/startStopGyro?run=:state'
            }
        }
    );
}
]);

app.controller('MainCtrl', ['$scope', 'Entry', 'StartStopSrv', 'poller',
    function ($scope, Entry, StartStopSrv, poller) {
    var pollerDelay = 110;
    $scope.pollerState = false;
    $scope.gyroState = false;
    var chartProbesCount = 0;
    var chartSpan = 10;

    $scope.data = {
        dataset0: [
        ]};

    $scope.accXChartOptions = {
      series: [
        {
          axis: "y",
          dataset: "dataset0",
          key: "accX",
          label: "aX",
          color: "#00FF00",
          type: ['line'],
          id: 'seriesAccX'
        },
        {
           axis: "y",
           dataset: "dataset0",
           key: "accXraw",
           label: "aXraw",
           color: "#FF0000",
           type: ['line'],
           id: 'seriesAccXraw'
        }
      ],
      axes: {
        x: {
            key: "x",
            min: 0,
            max: chartSpan,
        }
      },
      pan: {
            x: true,
            y: false
      }
    };

    $scope.clear = function(){
        chartProbesCount = 0;
        $scope.data.dataset0 = [];
        $scope.accXChartOptions.axes.x.min=0;
        $scope.accXChartOptions.axes.x.max=chartSpan;
    };

    $scope.editChart = function() {
        $scope.data.dataset0[1].accX = 666;
    };

    var myPoller = poller.get(Entry, {
        delay: pollerDelay,
        action: 'myQuery'
    });

    myPoller.promise.then(null, null, function(response){
        for(i=0; i < response.length; i++){
            if(response[i].type=="GYRO_RAW") {
                addGyroRawReading(response[i].value);
            }
            if(response[i].type=="GYRO_CLEAN") {
                addGyroCleanReading(response[i].value);
            }
        }
    });

    function addGyroRawReading(rawReading){
            $scope.data.dataset0.push({
                x: chartProbesCount,
                accX: rawReading.accX,
                accXraw: 0
            });
    }

     function addGyroCleanReading(rawReading){
            console.log("data length"+$scope.data.dataset0.length);
            $scope.data.dataset0[$scope.data.dataset0.length-1].accXraw=rawReading.accX;

            chartProbesCount++;
            if(chartProbesCount > chartSpan){
                $scope.accXChartOptions.axes.x.min++;
                $scope.accXChartOptions.axes.x.max++;
            }
        }

    $scope.runGyro = function(){
        $scope.gyroState = !$scope.gyroState;
        StartStopSrv.startOrStop(
            {
                state: $scope.gyroState
            }
        );
    }

    $scope.runPoller = function(){
        $scope.pollerState =! $scope.pollerState;
        if($scope.pollerState){
            myPoller.start();
        }
        else {
            myPoller.stop();
        }
    };
    myPoller.stop();//TODO: why is that for here?
}]);