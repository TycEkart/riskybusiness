var app = angular.module('boardApp', ['ui.bootstrap']);    
 
 

app.controller('boardCtrl', function($scope, $http,$uibModal) {
    
    $scope.myPlayerName = ""; 

    $scope.showLoginScreen = true;
    $scope.showStartTurnScreen = false;
    $scope.showAttackScreen = false;
    $scope.showMoveScreen = false;
    $scope.showWaitScreen = false;

    $scope.errorField="";
    $scope.test;
    
    $scope.board = [];
    
    $scope.attackFrom = -1;
    $scope.attackTo = -1;

    $scope.moveFrom = -1;
    $scope.moveTo = -1;
    $scope.amountToMove = 1; //init
    
    var PRINT_CONST = 0.7; //used to print a field at location
     
    
    //Prints an intial board. Note: this REST service may be temporary!
    $scope.printInitialBoard = function() {
        var request = $http({
            method: "GET",
            url: "http://localhost:8080/RiskyBusiness_RestWeb/rest/riskybusiness/createGame",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        });
        request.success(function(response) {
            //$scope.test = "ok1";
            //printBoard(response.data.board);
            $scope.board = response.data.board;
            angular.forEach($scope.board, function(value, key) {
              $scope.board[key].x = $scope.board[key].x * PRINT_CONST;
              $scope.board[key].y = $scope.board[key].y * PRINT_CONST;
              $scope.board[key].url = stringToColor($scope.board[key].owner);
            });
            //console.log($scope.board[4].x);
			console.log($scope.board);
        });
        request.error(function(response) {
                $scope.test = response.returncode;
                $scope.errorField = response.errormessage;
        });
    }
printBoard = function(board) {
        $scope.board = board;
        angular.forEach($scope.board, function(value, key) {
          $scope.board[key].x = $scope.board[key].x * PRINT_CONST;
          $scope.board[key].y = $scope.board[key].y * PRINT_CONST;
          $scope.board[key].url = stringToColor($scope.board[key].owner); 
        });
    }
    //log in. kind of...
    $scope.login = function() {
        $scope.myPlayerName = $scope.submittedUsername;
        $scope.showLoginScreen = false;
        $scope.showStartTurnScreen = true;
        console.log($scope.myPlayerName);
    }

    $scope.finishTurn = function() {
        angular.forEach($scope.board, function(value, key) {
           $scope.board[key].url = stringToColor($scope.board[key].owner); //reset color
        });
        var request = $http({
            method: "GET",
            url: "http://localhost:8080/RiskyBusiness_RestWeb/rest/riskybusiness/endTurn.php",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        });
        request.success(function(response) {
            //$scope.test = "ok4";
            console.log("finished turn");
            $scope.showMoveScreen = false;
            $scope.showWaitScreen = true;
        });
        request.error(function(response) {
            $scope.test = response.returncode;
            $scope.errorField = response.errormessage;
        });
    }
    
    attackArea = function(areaToMoveFrom, areaToAttack) {
        var request = $http({
            method: "GET",
            url: "http://localhost:8080/RiskyBusiness_RestWeb/rest/riskybusiness/attackarea/" + areaToMoveFrom + "/" + areaToAttack,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        });
        request.success(function(response) {
            //$scope.test = "ok2";
            console.log("attacked!");
            $scope.attackFrom = -1; //re-init
            $scope.attackTo = -1; //re-init
        });
        request.error(function(response) {
                $scope.test = response.returncode;
                $scope.errorField = response.errormessage;
				console.log(response.errrormessage);
                $scope.attackFrom = -1; //re-init
                $scope.attackTo = -1; //re-init
        });
    }
    
    //Choose to pass the turn or attack
    $scope.startTurn = function(action) {
		
		var request = $http({
            method: "GET",
            url: "http://localhost:8080/RiskyBusiness_RestWeb/rest/riskybusiness/startturn",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        });
        request.success(function(response) {
            //$scope.test = "ok";

        });
        request.error(function(response) {
                $scope.test = response.returncode;
                $scope.errorField = response.errormessage;
        });
		
		
        var request = $http({
            method: "GET",
            url: "http://localhost:8080/RiskyBusiness_RestWeb/rest/riskybusiness/setAttack",
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        });
        request.success(function(response) {
            //$scope.test = "ok";
            if (action == "attack") {
                $scope.showStartTurnScreen = false;
                $scope.showAttackScreen = true;
            }
            else {
                $scope.showStartTurnScreen = false;
                $scope.showWaitScreen = true;
            }
        });
        request.error(function(response) {
                $scope.test = response.returncode;
                $scope.errorField = response.errormessage;
        });
    }
    
    $scope.goToMoveScreen = function() {
        $scope.showAttackScreen = false;
        $scope.showMoveScreen = true;
        angular.forEach($scope.board, function(value, key) {
            $scope.board[key].url = stringToColor($scope.board[key].owner); //reset color
        });
    }
    
   $scope.attackAction = function(fieldID) {
        if ($scope.attackFrom == -1) { //no initial field selected yet
            if ($scope.board[fieldID].owner != $scope.myPlayerName)
                return; //because this is not your field!
            if ($scope.board[fieldID].numberOfTroops < 1)
                return; //because you cannot attack with 0 troops
            $scope.attackFrom = fieldID;
            //double loop to check adjacent fields:
            angular.forEach($scope.board, function(value, key) {
                angular.forEach($scope.board[fieldID].adjacentFields, function(value2, key2) {
                    if (($scope.board[key].id == $scope.board[fieldID].adjacentFields[key2]) && ($scope.board[key].owner != $scope.myPlayerName))
                        $scope.board[key].url = "app/content/images/redAreaSelectorHighlight.png";
                });
            });
            return;
        }
        else {
            if ($scope.attackFrom == fieldID) {
                $scope.attackFrom = -1; //= undo
                angular.forEach($scope.board, function(value, key) {
                    $scope.board[key].url = stringToColor($scope.board[key].owner); //reset color
                });
                return;
            }
            if ($scope.board[fieldID].owner == $scope.myPlayerName) //don't attack yourself :)
                return;
            angular.forEach($scope.board[$scope.attackFrom].adjacentFields, function(value2, key2) {
                if ($scope.board[$scope.attackFrom].adjacentFields[key2] == fieldID) { //if indeed adjacent, we can attack!
                    $scope.attackTo = fieldID;
                    attackArea($scope.attackFrom, $scope.attackTo);
                    angular.forEach($scope.board, function(value, key) {
                       $scope.board[key].url = stringToColor($scope.board[key].owner); //reset color
                    });
                }
                console.log('b');
            });
            
        }
    }
    
    $scope.moveAction = function(fieldID) {
        if ($scope.moveFrom == -1) { //no initial field selected yet
            if ($scope.board[fieldID].owner != $scope.myPlayerName)
                return; //because this is not your field!
            if ($scope.board[fieldID].numberOfTroops < 1)
                return; //because you cannot move 0 troops
            $scope.moveFrom = fieldID;
            //double loop to check adjacent fields:
            angular.forEach($scope.board, function(value, key) {
                angular.forEach($scope.board[fieldID].adjacentFields, function(value2, key2) {
                    if (($scope.board[key].id == $scope.board[fieldID].adjacentFields[key2]) && ($scope.board[key].owner == $scope.myPlayerName || $scope.board[key].numberOfTroops == 0)) //is the field adjacent + yours, or with 0 enemies?
                        $scope.board[key].url = "app/content/images/redAreaSelectorHighlight.png";
                });
            });
            return;
        }
        else {
            if ($scope.moveFrom == fieldID) {
                $scope.moveFrom = -1; //= undo
                angular.forEach($scope.board, function(value, key) {
                   $scope.board[key].url = stringToColor($scope.board[key].owner); //reset color
                });
                return;
            }
            if ($scope.board[fieldID].owner != $scope.myPlayerName && $scope.board[fieldID].numberOfTroops != 0) //should be your own field (or with 0 enemies)
                return;
            angular.forEach($scope.board[$scope.moveFrom].adjacentFields, function(value2, key2) {
                if ($scope.board[$scope.moveFrom].adjacentFields[key2] == fieldID) { //if indeed adjacent, we can move!
                    $scope.moveTo = fieldID;
                    console.log("Move from " + $scope.moveFrom + " to field " + $scope.moveTo + ".");
                    $scope.amountToMove = $scope.board[$scope.moveFrom].numberOfTroops;
                    console.log("aaa " + $scope.board[$scope.moveFrom].numberOfTroops);
                    $scope.showMoveScreenHowManyPopup = true;
                     var modalInstance = $uibModal.open({
                        templateUrl: 'myModalContent.html',
                        size:"sm",
                        scope:$scope
                        
                    });
                    $scope.close=function(){
                        modalInstance.dismiss();//$scope.modalInstance.close() also works I think
                    };
                    $scope.submitMove = function() {
                        moveTroops($scope.moveFrom, $scope.moveTo, $scope.amountToMove);
                        $scope.showMoveScreenHowManyPopup = false;
                        angular.forEach($scope.board, function(value, key) {
                            $scope.board[key].url = stringToColor($scope.board[key].owner);//reset color
                        });
                        modalInstance.dismiss();
                        console.log("test: "  + $scope.moveFrom);
        
                     }
                    //moveTroops($scope.moveFrom, $scope.moveTo, 1);
                }
            });
            
        }
    }

    moveTroops = function(moveFrom, moveTo, numberToMove) {
        console.log("test: "  + $scope.moveFrom);
        var request = $http({
            method: "GET",
            url: "http://localhost:8080/RiskyBusiness_RestWeb/rest/riskybusiness/movetroops/" + moveFrom + "/" + moveTo + "/" + numberToMove,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        });
        request.success(function(response) {
            //$scope.test = "ok3";
            console.log("moved.");
            printBoard(response.data.board);
            $scope.moveFrom = -1; //re-init
            $scope.moveTo = -1; //re-init
        });
        request.error(function(response) {
                $scope.test = response.returncode;
                $scope.errorField = response.errormessage;
                $scope.moveFrom = -1; //re-init
                $scope.moveTo = -1; //re-init
        });
    }

    
  
    var stringToColor = function(str) {
     var image;
     if(str=="red"){
        image = "app/content/images/redAreaSelector.png";
     }
     if(str=="green"){
        image = "app/content/images/greenAreaSelector.png";
     }
     if(str=="grey"){
        image = "app/content/images/greyAreaSelector.png";
     }
     if(str=="yellow"){
        image = "app/content/images/yellowAreaSelector.png";
     }
     if(str=="orange"){
        image = "app/content/images/orangeAreaSelector.png";
     }
     if(str=="blue"){
        image = "app/content/images/blueAreaSelector.png";
     }
      return image;
    }
    $scope.printInitialBoard();
});