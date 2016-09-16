var app = angular.module('flightModule', ['flightCtrlServices', 'ngResource', 'n3-line-chart', 'emguo.poller']);



app.controller('flightCtrl', ['$scope', 'poller', 'flightCtrlSrv', 'Entry', 'SingleChartSrv',
function($scope, poller, flightCtrlSrv, Entry, SingleChartSrv) {
    var pollerDelay = 100;
    var isRunning = false;

    $scope.motorPwrChart = new SingleChartSrv("motorPwr");
    $scope.angleChart = new SingleChartSrv("angle");
    $scope.regulationChart = new SingleChartSrv("regulation");

    var myPoller = poller.get(Entry, {
        delay: pollerDelay,
        action: 'getFlightEvents'
    });

    myPoller.start();

    myPoller.promise.then(null, null, function(response){
        for(i = 0; i < response.length; i++){
            if(response[i].type=="MOTOR_POWER") {
                $scope.motorPwrChart.addReading(response[i].value);
            }
            if(response[i].type=="FLIGHT_CONTROLLER_ANGLE") {
                $scope.angleChart.addReading(response[i].value);
            }
            if(response[i].type=="REGULATION") {
                $scope.regulationChart.addReading(response[i].value);
            }
        }
    });

    $scope.startStopFlightController = function(){
        $scope.isRunning = !$scope.isRunning;
            flightCtrlSrv.startStop(
            {
                state: $scope.isRunning
            }
        );
    };

    $scope.clearCharts = function(){
        $scope.motorPwrChart.clear();
        $scope.angleChart.clear();
        $scope.regulationChart.clear();
    };

    $scope.setDesiredAngle = function(angle){
        flightCtrlSrv.setDesiredAngle(
            {
                state: angle
            }
        );
    };

    $scope.setCurrentAngle = function(angle){
        flightCtrlSrv.setCurrentAngle(
            {
                state: angle
            }
        );
    };

    $scope.setP = function(value){
        flightCtrlSrv.setP(
            {
                state: value
            }
        );
    };

    $scope.setI = function(value){
        flightCtrlSrv.setI(
            {
                state: value
            }
        );
    };

    $scope.setD = function(value){
        flightCtrlSrv.setD(
            {
                state: value
            }
        );
    };
}]);