var app = angular.module("operation.manageDaily",[]);
app.controller("controllerManageDaily",["$scope","$http","utilService",function ($scope,$http,utilService) {
    $scope.baseURL = utilService.baseURL();
    $scope.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    $scope.url = $scope.baseURL+"bg/userLog?token="+localStorage.token+"&type=2&pageNum="+$scope.index+"&pageSize=5";
    utilService.requestData($scope);
    //点击下一页
    $("#pageLftUp").on("click",function () {
        $scope.index = $scope.downPge;
        sessionStorage.setItem("index",$scope.index);
        $scope.url =$scope.baseURL+"bg/userLog?token="+localStorage.token+"&type=2&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        sessionStorage.setItem("index",$scope.index);
        $scope.url = $scope.baseURL+"bg/userLog?token="+localStorage.token+"&type=2&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });
    //点击具体某个页码
    //就是用事件监听的方法才能给动态创建的方法绑定事件
    $(document).on("click",".current1",function(){
        $(this).addClass("current").siblings().removeClass("current");
        $scope.index = $(this).text();
        sessionStorage.setItem("index",$scope.index);
        $scope.url = $scope.baseURL+"bg/userLog?token="+localStorage.token+"&type=2&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });
    //故障类型转换
    $scope.formatDescription = function (str,way) {
        return utilService.formatDescription(str,way);
    }
    //按订单号查询订单
    $scope.referOrder = function () {
        var referOrder = $("#seletNameValue").val();
        console.log(referOrder);
        $scope.url =$scope.baseURL+"bg/getOrderByOrderNo?token="+localStorage.token+"&orderNo="+referOrder;
        return utilService.referOrder($scope);
    };
    //时间戳转换
    $scope.getLocalTime = function(ns) {
        var date = new Date(ns);
        Y = date.getFullYear() + '-';
        M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        D = date.getDate() + ' ';
        h = date.getHours() + ':';
        m = date.getMinutes()
        return time = Y+M+D+h+m;
    }
}]);