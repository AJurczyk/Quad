var app = angular.module('myApp', ['ngResource', 'n3-line-chart']);

app.factory('Entry', function($resource) {
    //return $resource('/hello?name=:name');
    return $resource('/gyro');
});


app.controller('MainCtrl', ['$scope', 'Entry',
    function ($scope, Entry) {
    $scope.name = "---";
    $scope.readings = [];
    var counter = 0;

    $scope.getGyro = function(){
        var resp = Entry.get({},
            function(){
                $scope.readings.push(resp);
                $scope.data.dataset0.push({x: counter, val_0: resp.accX});
                counter+=1;
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
          key: "val_0",
          label: "An area series",
          color: "#1f77b4",
          type: ['line'],
          id: 'mySeries0'
        }
      ],
      axes: {
        x: {
            key: "x"
        }
      }
    };
    $scope.clear = function(){
        counter = 0;
        $scope.data.dataset0=[];
        $scope.readings = [];
    };

}]);