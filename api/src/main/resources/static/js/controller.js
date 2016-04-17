var app = angular.module('myApp', ['ngResource', 'n3-line-chart', 'emguo.poller']);

app.filter('reverse', function() {
  return function(items) {
    return items.slice().reverse();
  };
});

app.factory('Entry', function($resource) {
    return $resource('/getMeasurements',[], {
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
          color: "#00FFFF",
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
          color: "#00FFFF",
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
        },



        {
                  axis: "y",
                  dataset: "dataset0",
                  key: "accXraw",
                  label: "aXraw",
                  color: "#FF0000",
                  type: ['line'],
                  id: 'seriesAccXraw'
                },
                {
                  axis: "y",
                  dataset: "dataset0",
                  key: "accYraw",
                  label: "aYraw",
                  color: "#FF0000",
                  type: ['line'],
                  id: 'seriesAccYraw'
                 },
                {
                  axis: "y",
                  dataset: "dataset0",
                  key: "accZraw",
                  label: "aZraw",
                  color: "#FF0000",
                  type: ['line'],
                  id: 'seriesAccZraw'
                },
                {
                  axis: "y",
                  dataset: "dataset0",
                  key: "gyroXraw",
                  label: "gXraw",
                  color: "#FF0000",
                  type: ['line'],
                  id: 'seriesGyroXraw'
                },
                {
                  axis: "y",
                  dataset: "dataset0",
                  key: "gyroYraw",
                  label: "gYraw",
                  color: "#FF0000",
                  type: ['line'],
                  id: 'seriesGyroYraw'
                 },
                {
                  axis: "y",
                  dataset: "dataset0",
                  key: "gyroZraw",
                  label: "gZraw",
                  color: "#FF0000",
                  type: ['line'],
                  id: 'seriesGyroZraw'
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
        $scope.data.dataset0 = [];
        $scope.readings = [];
        $scope.options.axes.x.min=0;
        $scope.options.axes.x.max=chartSpan;
    };

    var myPoller = poller.get(Entry, {
        delay: pollerDelay,
        action: 'myQuery'
    });
    myPoller.promise.then(null, null, function(response){
        addGyroReading(response);
    });

    function addGyroReading(resp){
        for(i=0; i < resp.length; i+=2){
            $scope.readings.push(resp[i]);
            $scope.data.dataset0.push({
                x: counter,
                accX: resp[i].accX,
                accY: resp[i].accY,
                accZ: resp[i].accZ,
                gyroX: resp[i].gyroX,
                gyroY: resp[i].gyroY,
                gyroZ: resp[i].gyroZ,

                accXraw: resp[i+1].accX,
                accYraw: resp[i+1].accY,
                accZraw: resp[i+1].accZ,
                gyroXraw: resp[i+1].gyroX,
                gyroYraw: resp[i+1].gyroY,
                gyroZraw: resp[i+1].gyroZ});

            if(counter > chartSpan){
                $scope.options.axes.x.min++;
                $scope.options.axes.x.max++;
            }
            counter++;
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