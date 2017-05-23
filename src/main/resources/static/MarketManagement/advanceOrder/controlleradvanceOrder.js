var app = angular.module("marketManage.advanceOrder",[
    'ui.router',
    'starter.services'
]);
app.controller("controlleradvanceOrder",["$scope","$http","utilService","$state",function ($scope,$http,utilService,$state) {
    $(".selectName1").change(function () {
        var oVale = $(this).find("option:selected").text();
        $("#selectStaffName").text(oVale);
    });
    console.log(localStorage.token);
    $scope.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    $scope.baseURL = utilService.baseURL();
    $scope.orderRefer = false;
    $scope.orderList = true;
    if ($scope.orderList){
        $scope.url = $scope.baseURL+"bg/getAppointmentOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
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
    //http://192.168.3.108:8080/qcwy/bg/getAppointmentOrders?token=aaa&pageNum=1&pageSize=1
    //点击下一页
    $("#pageLftUp").on("click",function () {
        $scope.index = $scope.downPge;
        $scope.url = $scope.baseURL+"bg/getAppointmentOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        $scope.url = $scope.baseURL+"bg/getAppointmentOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击具体某个页码
    // $scope.itemData = sessionStorage.itemData;
    // console.log(sessionStorage.itemData);
    // console.log($scope.itemData+"12315");
    //就是用事件监听的方法才能给动态创建的方法绑定事件
    $(document).on("click",".current1",function(){
        $(this).addClass("current").siblings().removeClass("current");
        $scope.index = $(this).text();
        sessionStorage.setItem("index",$scope.index);
        $scope.url = $scope.baseURL+"bg/getPartList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });

    //问题类型转化
    $scope.formatDescription = function (str,way) {
        return utilService.formatDescription(str,way);
    }
    $scope.distributeOver = function (lati,lon,orderNo) {
        var obj = {};
        obj.lati = lati;
        obj.lon = lon;
        obj.orderNo = orderNo
        console.log(obj);
        var jsonString = angular.toJson(obj);
        sessionStorage.setItem("jsonString",jsonString);
        console.log(jsonString);
        sessionStorage.setItem("infoWindowBtn",true);
        $state.go("operation.masterMap")
    }
}]);
