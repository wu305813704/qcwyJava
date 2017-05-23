var app = angular.module("callCenter.waitOrder",[]);
app.controller("controllerWaitOrder",["$scope","$http","utilService","$state",function ($scope,$http,utilService,$state)  {
    $(".selectName1").change(function () {
        var oVale = $(this).find("option:selected").text();
        $("#selectStaffName").text(oVale);
    });

    //引入公用请求链接
    $scope.baseURL = utilService.baseURL();
    //刷新时保留原有的状态
    $scope.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    //刚开始进入界面要显示的数据
    $scope.orderRefer = false;
    $scope.orderList = true;
    if ($scope.orderList){
        $scope.url = $scope.baseURL+"bg/getDistributeOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    }
    $scope.referOrder = function () {
        $scope.itemData = [];
        $scope.orderRefer = true;
        $scope.orderList = false;
        var referOrder = $("#seletNameValue").val();
        console.log(referOrder);
        $scope.url =$scope.baseURL+"bg/getOrderByOrderNo?token="+localStorage.token+"&orderNo="+referOrder;
        utilService.referOrder($scope);
    };
    //点击下一页
    $("#pageLftUp").on("click",function () {
        $scope.index = $scope.downPge;
        sessionStorage.setItem("index",$scope.index);
        $scope.url = $scope.baseURL+"bg/getDistributeOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        sessionStorage.setItem("index",$scope.index);
        $scope.url = $scope.baseURL+"bg/getDistributeOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //就是用事件监听的方法才能给动态创建的方法绑定事件
    $(document).on("click",".current1",function(){
        $(this).addClass("current").siblings().removeClass("current");
        $scope.index = $(this).text();
        sessionStorage.setItem("index",$scope.index);
        $scope.url = $scope.baseURL+"bg/getDistributeOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
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
    //数据转换
    $scope.formatDescription = function (str,way) {
        return utilService.formatDescription(str,way);
    };
    //点击进行派单  并调到地图页面
    $scope.distribute = function (lati,lon,orderNo) {
        var obj = {};
        obj.lati = lati;
        obj.lon = lon;
        obj.orderNo = orderNo;
        var jsonString = angular.toJson(obj);
        sessionStorage.setItem("jsonString",jsonString);
        //判断地图界面
        sessionStorage.setItem("infoWindowBtn",true);
        $state.go("operation.masterMap")
    }
}]);
