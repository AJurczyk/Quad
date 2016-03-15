var app = angular.module('myApp', []);

app.controller('myCtrl', function ($scope) {

    $scope.name = "";

    $scope.sayName = function() {
    $scope.name = "WIELKI";
    }

});