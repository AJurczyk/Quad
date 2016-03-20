var app = angular.module('myApp', ['ngResource']);

app.factory('Entry', function($resource) {
    //return $resource('/hello?name=:name');
    return $resource('/gyro');
});


app.controller('MainCtrl', ['$scope', 'Entry',
    function ($scope, Entry) {
    $scope.name = "---";
    $scope.readings = [];

    $scope.getGyro = function(){
        var resp = Entry.get({},
            function(){
                $scope.readings.push(resp);
            },
            function(){
                $scope.name = "error";}
        );
    };
}]);