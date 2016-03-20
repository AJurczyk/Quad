var app = angular.module('myApp', ['ngResource', 'chart.js']);

app.factory('Entry', function($resource) {
    //return $resource('/hello?name=:name');
    return $resource('/gyro');
});


app.controller('MainCtrl', ['$scope', 'Entry',
    function ($scope, Entry) {
    $scope.name = "---";
    $scope.readings = [];
    $scope.labels = [];
    $scope.data = [[]];
    var counter = 0;

    $scope.getGyro = function(){
        var resp = Entry.get({},
            function(){
                $scope.readings.push(resp);
                $scope.labels.push(counter++);
                $scope.data[0].push(resp.accX);
            },
            function(){
                $scope.name = "error";}
        );
    };

    $scope.clear = function(){
        $scope.readings = [];
        $scope.labels = [];
        $scope.data = [[]];
        counter = 0;
    }
}]);