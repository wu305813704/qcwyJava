var app = angular.module("basicData.basicDataBuy",[]);
app.controller("controllerBasicDataBuy",["$scope","$http",function ($scope,$http) {
    if (sessionStorage.number2 == undefined){
        $(".span5"+0).addClass("on").siblings().removeClass("on");
    }else {
        $(".span5"+sessionStorage.number2).addClass("on").siblings().removeClass("on");
    }
    $(".span400_1_1").on("click",function () {
        $(this).addClass("on").siblings().removeClass("on");
    });
    $(".sapn400queren").on("click",function () {
        alert("功能还没开放，敬请期待！！！");
    });
    $(".sapn400reset").on("click",function () {
        alert("功能还没开放，敬请期待！！！");
    });
    $(".top_PFl span").on("click",function () {
        $(this).addClass("on").siblings().removeClass("on");
        sessionStorage.setItem("number2",$(".top_PFl span").index($(this)));
        $scope.classify = $(".top_PFl span").index($(this))+1;
        sessionStorage.setItem("classify2",$scope.classify);
        // $scope.url = "http://192.168.3.108:8080/qcwy/bg/getPartList?token=aaa&pageNum="+$scope.index+"&pageSize=5&classify="+$scope.classify;
        // utilService.requestData($scope);
    });
}]);