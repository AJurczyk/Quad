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
    var chartSpan = 10;

    $scope.getGyro = function(){
        var resp = Entry.get({},
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
          label: "X",
          color: "#FF0000",
          type: ['line'],
          id: 'seriesAccX'
        },
        {
          axis: "y",
          dataset: "dataset0",
          key: "accY",
          label: "Y",
          color: "#00FF00",
          type: ['line'],
          id: 'seriesAccY'
         },
        {
          axis: "y",
          dataset: "dataset0",
          key: "accZ",
          label: "Z",
          color: "#0000FF",
          type: ['line'],
          id: 'seriesAccZ'
        }
      ],
      axes: {
        x: {
            key: "x",
            min: 0,
            max: chartSpan
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
        $scope.data.dataset0.push({x: counter, accX: resp.accX, accY: resp.accY, accZ: resp.accZ});
        if(counter > chartSpan){
            $scope.options.axes.x.min++;
            $scope.options.axes.x.max++;
        }
        counter++;
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