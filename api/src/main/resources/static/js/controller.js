var app = angular.module('myApp', ['ngResource', 'n3-line-chart', 'emguo.poller']);

app.filter('reverse', function() {
  return function(items) {
    return items.slice().reverse();
  };
});

app.factory('Entry', function($resource) {
    //return $resource('/hello?name=:name');
    return $resource('/gyro');
});

app.controller('MainCtrl', ['$scope', 'Entry', 'poller',
    function ($scope, Entry, poller) {
    var delay = 10;
    $scope.pollerState = false;
    $scope.readings = [];
    var counter = 0;
    var chartSpan = 100;

    $scope.getGyro = function(){
        var resp = Entry.query({},
            function(){
                addGyroReading(resp);
            },
            function(){
                $scope.name = "error";}
        );
    };

    $scope.data = {
        dataset0: [
        ]};

    $scope.options = {
      series: [
        {
          axis: "y",
          dataset: "dataset0",
          key: "accX",
          label: "aX",
          color: "#FF0000",
          type: ['line'],
          id: 'seriesAccX'
        },
        {
          axis: "y",
          dataset: "dataset0",
          key: "accY",
          label: "aY",
          color: "#00FF00",
          type: ['line'],
          id: 'seriesAccY'
         },
        {
          axis: "y",
          dataset: "dataset0",
          key: "accZ",
          label: "aZ",
          color: "#0000FF",
          type: ['line'],
          id: 'seriesAccZ'
        },
        {
          axis: "y",
          dataset: "dataset0",
          key: "gyroX",
          label: "gX",
          color: "#FF0000",
          type: ['line'],
          id: 'seriesGyroX'
        },
        {
          axis: "y",
          dataset: "dataset0",
          key: "gyroY",
          label: "gY",
          color: "#00FF00",
          type: ['line'],
          id: 'seriesGyroY'
         },
        {
          axis: "y",
          dataset: "dataset0",
          key: "gyroZ",
          label: "gZ",
          color: "#0000FF",
          type: ['line'],
          id: 'seriesGyroZ'
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
        counter = 0;
        $scope.data.dataset0=[];
        $scope.readings = [];
        $scope.options.axes.x.min=0;
        $scope.options.axes.x.max=chartSpan;
    };

    var myPoller = poller.get(Entry, {delay: delay});
    myPoller.promise.then(null, null, function(response){
        addGyroReading(response);
    });

    function addGyroReading(resp){
        $scope.readings.push(resp);
        for(i=0; i < resp.length; i++){
            $scope.data.dataset0.push({
                x: counter,
                accX: resp[i].accX,
                accY: resp[i].accY,
                accZ: resp[i].accZ,
                gyroX: resp[i].gyroX,
                gyroY: resp[i].gyroY,
                gyroZ: resp[i].gyroZ});

            if(counter > chartSpan){
                $scope.options.axes.x.min++;
                $scope.options.axes.x.max++;
            }
            counter++;
        }
    }

    $scope.run = function(){
    $scope.pollerState =! $scope.pollerState;
        if($scope.pollerState){
            myPoller.start();
        }
        else {
            myPoller.stop();
        }
    };
    myPoller.stop();
//    myPoller.start();
//    myPoller.restart();
//    myPoller.remove();
}]);