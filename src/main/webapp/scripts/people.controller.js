angular.module('myApp', ['ui.bootstrap']);
angular.module('myApp').controller('PeopleCtrl',
    function ($scope, $http, $location) {
        const model = {};
        $scope.model = model;
        $scope.countryCodes = ['372', '373'];

        $scope.formNames = [ {0: 'email'}, {1: 'phone'}, {2: 'clubone'} ]

        model.email = {}
        model.phone = {}
        model.clubone = {}
        model.email.email = null;
        model.phone.phone = null;
        model.phone.countryCode = null;
        model.clubone.clubOne = null;
        model.clubone.firstName = null;
        model.clubone.lastName = null;

        $scope.timeElapsed = null;
        $scope.requestInProgress = false;

        $scope.bestMatchClients = null;
        $scope.otherClients = [];

        if ($location.search().token) {
            $scope.token = $location.search().token;
            $location.search('token', null)
        }

        $scope.loadClients = function() {
            var startTime = new Date();
            $scope.requestInProgress = true;
            //
            var parameters = model[$scope.formNames[$scope.activeForm][$scope.activeForm]];

            $http.get('/api/list',
                {
                    params: parameters
                }
            ).then(function(response) {
                    $scope.requestInProgress = false;
                    var endTime = new Date();
                    var diffTime = endTime - startTime;
                    $scope.timeElapsed = diffTime;
                if ( response.status == 200 ) {
                    if (response.data["Best match"])
                            $scope.bestMatchClients = response.data["Best match"];
                        else
                            $scope.bestMatchClients = [];

                        if (response.data["Others"])
                            $scope.otherClients = response.data["Others"];
                        else
                            $scope.otherClients = [];

                    } else {
                        var endTime = new Date();
                        var diffTime = endTime - startTime;
                        $scope.timeElapsed = diffTime;
                        alert("Something is wrong, status code:"+response.statusText);
                    }
                }, function(x) {
                    $scope.requestInProgress = false;
                    var endTime = new Date();
                    var diffTime = endTime - startTime;
                    $scope.timeElapsed = diffTime;
                    alert("error:"+ x.statusText);
                });
        }
    }
);
