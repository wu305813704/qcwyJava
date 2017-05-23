var app = angular.module("marketManage.confirm",[]);
app.controller("controllerconfirm",["$scope","$http","utilService","$state",function ($scope,$http,utilService,$state) {
    $(".selectName1").change(function () {
        var oVale = $(this).find("option:selected").text();
        $("#selectStaffName").text(oVale);
    });
    $(".span400_1_1").on("click",function () {
        $(this).addClass("on").siblings().removeClass("on");
    });
    $(".addStaff_TopR").on("click",function () {
        $(".mark1").css("display","none");
        $("#confirmOrderDescription").css("display","none");
    });
    //去掉字符串左右两端的空格
    function trim(str){
        return str.replace(/(^\s*)|(\s*$)/g, "");
    }
    var arr = document.getElementsByClassName("top_pType");
    //判断不同状态显示的颜色
    for(var i = 0; i< arr.length;i++){
        if (trim(arr[i].innerHTML) == "顾客取消"){
            arr[i].style.color = "red";
        }else if (trim(arr[i].innerHTML) == "工程师取消"){
            arr[i].style.color = "green";
        }
    }
    //http://192.168.3.108:8080/qcwy/bg/getAfterSaleOrders?token=aaa&pageNum=1&pageSize=2
    $scope.baseURL = utilService.baseURL();
    $scope.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    $scope.orderRefer = false;
    $scope.orderList = true;
    if ($scope.orderList){
        $scope.url = $scope.baseURL+"bg/getAfterSaleOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
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
        $scope.url = $scope.baseURL+"bg/getAfterSaleOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        $scope.url = $scope.baseURL+"bg/getAfterSaleOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });
    //点击具体某个页码
    //就是用事件监听的方法才能给动态创建的方法绑定事件
    $(document).on("click",".current1",function(){
        $(this).addClass("current").siblings().removeClass("current");
        $scope.index = $(this).text();
        $scope.url = $scope.baseURL+"bg/getAfterSaleOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });
    //故障类型转换
    $scope.formatDescription = function (str,way) {
        return utilService.formatDescription(str,way);
    };
    //点击驳回窗口
    $scope.orderReject = function (msg) {
        $scope.orderNo =msg;
        $(".mark1").css("display","block");
        $("#bohui").css("display","block");
    };
    //点击确认弹窗
    $scope.orderAffirm = function (lati,lon,orderNo) {
        var obj = {};
        obj.lati = lati;
        obj.lon = lon;
        obj.orderNo = orderNo;
        console.log(obj);
        var jsonString = angular.toJson(obj);
        sessionStorage.setItem("jsonString",jsonString);
        console.log(jsonString);
        sessionStorage.setItem("infoWindowBtn",true);
        $state.go("operation.masterMap")
    };
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
                break;
            case 10:
                return "用户验收";
                break;
            default:
                return "已付款"
        }
    }
    $scope.reasonReject = function () {
        $scope.reason = $("#reasonReject").val();
        $http.get($scope.baseURL+"bg/rejectOrder?token="+localStorage.token+"&orderNo="+$scope.orderNo+"&cause="+$scope.reason).success(function (res) {
            if (res.state == 0){
                $(".mark1").css("display","none");
                $("#bohui").css("display","none");
            }
        })
    };
    $scope.resetReject = function () {
        $("#reasonReject").text("");
        $(".mark1").css("display","none");
        $("#bohui").css("display","none");
    }
}]);