var app = angular.module("operation.masterRank",[]);
app.controller("controllerMasterRank",["$scope","$http","utilService",function ($scope,$http,utilService) {
    $scope.baseURL = utilService.baseURL();
    $scope.ArrayData = [];
    $scope.ArrarNmae = [];
    var objName = {};
    var objData = {};
    var masterPANK = document.getElementsByClassName("masterPANK");
    var masterPANK2 = document.getElementsByClassName("masterPANK2");
    var imh = document.getElementsByClassName("imh")
    $http.get($scope.baseURL+"bg/getOrderCountRank?token="+localStorage.token+"&date=2017").success(function (res) {
        var data1 = res.data;
        $scope.ArrayData = res.data;
        for (var i = 0;i<data1.length;i++){
            if(data1[i].rank == 1){
                $scope.totalOrder = data1[i].score + 10;
            }
        }
    });
    setTimeout(function () {
        if(masterPANK.length > 0){
            for (var i = 0;i < imh.length;i++){
                if (i == 0){
                    imh[0].src = "../Quote/img/operation/jinpai.png"
                }else if(i == 1){
                    imh[1].src = "../Quote/img/operation/yinpai.png"
                }else if(i == 2){
                    imh[2].src = "../Quote/img/operation/tongpai.png"
                }
            }
        }
    },80);

    $scope.getImgUrl = function (rank) {
        if (rank == 1){
           return "../Quote/img/operation/jinpai.png"
        }else if(rank == 2){
            return "../Quote/img/operation/yinpai.png"
        }else if(rank == 3){
            return "../Quote/img/operation/tongpai.png"
        }
    }
    $("#marsterServiceTime").mouseover(function(){
        $("#marsterServiceTimeHiddle").show(1000);
    });
    $("#marsterServiceTimeHiddle .marsterServiceTimeHiddle1").on("click",function () {
        $(this).css("backgroundColor","#02a592").siblings().css("backgroundColor","");
        $("#marsterServiceTime").text($(this).text());
        $("#marsterServiceTimeHiddle").hide(1000);
    });
    $(".dingdanliang").on("click",function () {
        $http.get($scope.baseURL+"bg/getOrderCountRank?token="+localStorage.token+"&date=2017").success(function (res) {
            var data1 = res.data;
            $scope.ArrayData = res.data;
            for (var i = 0;i<data1.length;i++){
                if(data1[i].rank == 1){
                    $scope.totalOrder = data1[i].score + 10;
                }
            }
        });
    });
    $(".pingjunfen").on("click",function () {
        $http.get($scope.baseURL+"bg/getOrderScoreRank?token="+localStorage.token+"&date=2017").success(function (res) {
            var data1 = res.data;
            $scope.ArrayData = res.data;
            for (var i = 0;i<data1.length;i++){
                if(data1[i].rank == 1){
                    $scope.totalOrder = data1[i].score + 10;
                }
            }
        });
    })
}])