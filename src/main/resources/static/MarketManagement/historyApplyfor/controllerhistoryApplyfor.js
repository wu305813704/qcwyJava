var app = angular.module("marketManage.historyApplyfor",[]);
app.controller("controllerhistoryApplyfor",["$scope","$http","utilService",function ($scope,$http,utilService) {
    $(".selectName1").change(function () {
        var oVale = $(this).find("option:selected").text();
        $("#selectStaffName").text(oVale);
    });
    $("#historyApply").on("click",function () {
        $(".mark1").css("display","block");
        $("#confirmOrderDescription").css("display","block");
    });
    $(".addStaff_TopR").on("click",function () {
        $(".mark1").css("display","none");
        $("#confirmOrderDescription").css("display","none");
    });
    //http://192.168.3.108:8080/qcwy/bg/getHistoryOrders?token=aaa&pageNum=1&pageSize=5
    $scope.baseURL = utilService.baseURL();
    $scope.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    $scope.orderRefer = false;
    $scope.orderList = true;
    if ($scope.orderList){

        $scope.url = $scope.baseURL+"bg/getHistoryOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
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
        $scope.url = $scope.baseURL+"bg/getHistoryOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        $scope.url = $scope.baseURL+"bg/getHistoryOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });
    //点击具体某个页码
    $scope.itemData = sessionStorage.itemData;
    //就是用事件监听的方法才能给动态创建的方法绑定事件
    $(document).on("click",".current1",function(){
        $(this).addClass("current").siblings().removeClass("current");
        $scope.index = $(this).text();
        $scope.url = $scope.baseURL+"bg/getHistoryOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });
    //故障类型转换
    $scope.formatDescription = function (str,way) {
        return utilService.formatDescription(str,way);
    }
    //订单类型
    $scope.orderType = function (way) {
        switch (way){
            case 0:
                return "初始状态";
                break;
            case 1:
                return "已被抢";
                break;
            case 2:
                return "开始订单";
                break;
            case 3:
                return "暂停";
                break;
            case 4:
                return "预约";
                break;
            case 5:
                return "取消";
                break;
            case 6:
                return "改派";
                break;
            case 7:
                return "工程师确认故障";
                break;
            case 8:
                return "用户确认故障";
                break;
            case 9:
                return "维修完成";
                break
            case 10:
                return "用户验收";
                break
            default:
                return "已付款"
        }
    }

}]);