var app = angular.module("callCenter.allCenter",[]);
app.controller("controllerAllCenter",["$scope","$http","$state","utilService",function ($scope,$http,$state,utilService) {
        $(function () {
            $(".top_P6").on("click",function () {
                $(".mark1").css("display","block");
                $("#orderDescription").css("display","block");
            });
            $(".addStaff_TopR").on("click",function () {
                $(".mark1").css("display","none");
                $("#orderDescription").css("display","none");
            });
            $(".selectName1").change(function () {
                var oVale = $(this).find("option:selected").text();
                $("#selectStaffName").text(oVale);
            });
        });

        //跳转到地图界面
        $scope.toggle = function () {
            $state.go("operation.masterMap");
             $("#callCenter").removeClass("on");
            $("#operation").addClass("on");
        };
    //引入公用请求链接
    $scope.baseURL = utilService.baseURL();
    //刷新时保留原有的状态
    $scope.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    //刚开始进入界面要显示的数据
    //?token=aaa&pageNum=1&pageSize=5
    $scope.orderRefer = false;
    $scope.orderList = true;
    if ($scope.orderList){
        $scope.url = $scope.baseURL+"bg/getAllOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
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
        $scope.url = $scope.baseURL+"bg/getAllOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        sessionStorage.setItem("index",$scope.index);
        $scope.url = $scope.baseURL+"bg/getAllOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });
    //点击具体某个页码
    //就是用事件监听的方法才能给动态创建的方法绑定事件
    $(document).on("click",".current1",function(){
        $(this).addClass("current").siblings().removeClass("current");
        $scope.index = $(this).text();
        sessionStorage.setItem("index",$scope.index);
        $scope.url = $scope.baseURL+"bg/getAllOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });

    //数据转换
    $scope.formatDescription = function (str,way) {
        return utilService.formatDescription(str,way);
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
                break
            case 10:
                return "用户验收";
                break
            default:
                return "已付款"
        }
    }

}]);