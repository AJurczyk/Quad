var app = angular.module('myApp', ['quadServices','ngResource', 'n3-line-chart', 'emguo.poller']);

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

app.controller('MainCtrl', ['$scope', 'Entry', 'StartStopSrv', 'poller', 'GyroChartSrv', 'GyroChartsMgr',
    function ($scope, Entry, StartStopSrv, poller, GyroChartSrv, GyroChartsMgr) {

    $scope.ChartManager = GyroChartsMgr;

    var pollerDelay = 110;
    $scope.pollerState = false;
    $scope.gyroState = false;


    var myPoller = poller.get(Entry, {
        delay: pollerDelay,
        action: 'myQuery'
    });
    myPoller.stop();

    myPoller.promise.then(null, null, function(response){
        for(i=0; i < response.length; i++){
            if(response[i].type=="GYRO_RAW") {
                $scope.ChartManager.addGyroRawReading(response[i].value);
            }
            if(response[i].type=="GYRO_CLEAN") {
                $scope.ChartManager.addGyroCleanReading(response[i].value);
            }
        }
    });

    $scope.runGyro = function(){
        $scope.gyroState = !$scope.gyroState;
        StartStopSrv.startOrStop(
            {
                state: $scope.gyroState
            }
        );
    };


    $scope.runPoller = function(){
        $scope.pollerState =! $scope.pollerState;
        if($scope.pollerState){
            myPoller.start();
        }
        else {
            myPoller.stop();
        }
     };
}]);