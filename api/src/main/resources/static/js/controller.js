var app = angular.module('myApp', ['ngResource']);

app.factory('Entry', function($resource) {
    //return $resource('/hello?name=:name');
    return $resource('/gyro');
});

app.controller('MainCtrl', ['$scope', 'Entry',
    function ($scope, Entry) {
    $scope.name = "---";

    $scope.sayName = function(){
        var resp = Entry.get({},
            function(){
                console.log(resp);
                /*$scope.name = "ID: " + resp.id + ", name: " + resp.name;*/
            },
            function(){
                $scope.name = "error";}
        );
    };
}]);