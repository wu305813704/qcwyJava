var App = angular.module("marketManage.repairOder",[]);
App.controller("controllerRepairOrder",["$scope","$http","utilService",function ($scope,$http,utilService) {
    $(".selectName1").change(function () {
        var oVale = $(this).find("option:selected").text();
        $("#selectStaffName").text(oVale);
    });
    //去掉字符串左右两端的空格
    function trim(str){
        return str.replace(/(^\s*)|(\s*$)/g, "");
    }
    var arr = document.getElementsByClassName("top_pType");
    //判断不同状态显示的颜色
    for(var i = 0; i< arr.length;i++){
        if (trim(arr[i].innerHTML) == "暂停中"){
            arr[i].style.color = "red";
        }else if (trim(arr[i].innerHTML) == "改派中"){
            arr[i].style.color = "green";
        }
    }
    $scope.baseURL = utilService.baseURL();
    $scope.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    $scope.orderRefer = false;
    $scope.orderList = true;
    if ($scope.orderList){
        $scope.url = $scope.baseURL+"bg/getServiceOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    }
    $scope.referOrder = function () {
        $scope.itemData = [];
        $scope.orderRefer = true;
        $scope.orderList = false;
        var referOrder = $("#seletNameValue").val();
        console.log(referOrder);
        alert("一定要查出来奥")  ;
        $scope.url =$scope.baseURL+"bg/getOrderByOrderNo?token="+localStorage.token+"&orderNo="+referOrder;
        utilService.referOrder($scope);
    };
    //点击某个零件栏  获取具体零件的数据
    $(".top_PFl span").on("click",function () {
        $(this).addClass("on").siblings().removeClass("on");
        $scope.url = $scope.baseURL+"bg/getServiceOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击下一页
    $("#pageLftUp").on("click",function () {
        $scope.index = $scope.downPge;
        $scope.url = $scope.baseURL+"bg/getServiceOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        $scope.url = $scope.baseURL+"bg/getServiceOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击具体某个页码
    $scope.itemData = sessionStorage.itemData;
    //就是用事件监听的方法才能给动态创建的方法绑定事件
    $(document).on("click",".current1",function(){
        $(this).addClass("current").siblings().removeClass("current");
        $scope.index = $(this).text();
        $scope.url = $scope.baseURL+"bg/getPartList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5&classify="+$scope.classify;
        utilService.requestData($scope);
    });
    //转换数据
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