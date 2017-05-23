var app = angular.module("userManage.userManage1",[]);
app.controller("controllerUserManage",["$scope","$http","utilService",function ($scope,$http,utilService) {
    $(".selectName1").change(function () {
        var oVale = $(this).find("option:selected").text();
        $("#selectStaffName").text(oVale);
    });
    //http://192.168.3.108:8080/qcwy/bg/getWxUsers?token=aaa&pageNum=1&pageSize=9
    $scope.baseURL = utilService.baseURL();
    $scope.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    $scope.orderRefer1 = false;
    $scope.orderList1 = true;
    if ($scope.orderList1){
        $scope.url = $scope.baseURL+"bg/getWxUsers?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    }
    $scope.referOrder = function () {
        $scope.itemData = [];
        $scope.orderRefer1 = true;
        $scope.orderList1 = false;
        var referOrder = $("#seletNameValue").val();
        console.log(referOrder);
        $scope.url =$scope.baseURL+"bg/getOrderByOrderNo?token="+localStorage.token+"&orderNo="+referOrder;
        utilService.referOrder($scope);
    };

    //点击下一页
    $("#pageLftUp").on("click",function () {
        $scope.index = $scope.downPge;
        $scope.url = $scope.baseURL+"bg/getWxUsers?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        $scope.url = $scope.baseURL+"bg/getWxUsers?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击具体某个页码
    $scope.itemData = sessionStorage.itemData;
    //就是用事件监听的方法才能给动态创建的方法绑定事件
    $(document).on("click",".current1",function(){
        $(this).addClass("current").siblings().removeClass("current");
        $scope.index = $(this).text();
        $scope.url = $scope.baseURL+"bg/getWxUsers?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //故障类型转换
    $scope.formatDescription = function (str,way) {
        return utilService.formatDescription(str,way);
    };
    //时间戳转换
    $scope.getLocalTime = function(ns) {
        var date = new Date(ns);
        Y = date.getFullYear() + '-';
        M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        D = date.getDate() + ' ';
        h = date.getHours() + ':';
        m = date.getMinutes()
        if (ns == null || ns == undefined || ns == ""){
            return ''
        }else{
            return time = Y+M+D+h+m;
        }
    }
}]);