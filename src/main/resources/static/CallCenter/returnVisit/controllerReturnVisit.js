var app = angular.module("callCenter.returnVisit",[]);
app.controller("controllerReturnVisit",["$scope","$http","utilService",function ($scope,$http,utilService) {
    //引入公用请求链接
    $scope.baseURL = utilService.baseURL();
    $(function () {
        //点击进行筛选订单号
        $(".selectName1").change(function () {
            var oVale = $(this).find("option:selected").text();
            $("#selectStaffName").text(oVale);
        });
        //点击切换按钮背景色
        $(".span400_1_1").on("click",function () {
            $(this).addClass("on").siblings().removeClass("on");
        });
        //点击取消按钮弹框消失
        $(".addStaff_Btomreset").on("click",function () {
            $(".mark1").css("display","none");
            $("#addStaff").css("display","none");
        });
    });
    //点击回访弹窗里面的确定按钮  发送请求
    $scope.sentHuifang = function () {
        $http.get($scope.baseURL+"bg/returnVisit?token="+localStorage.token+"&userNo="+sessionStorage.userNo+"&orderNo="+$scope.orderNo+"&content="+$("#returnText").val()).success(function (res) {
            if (res.state == 0){
                $(".mark1").css("display","none");
                $("#addStaff").css("display","none");
                $scope.url = $scope.baseURL+"bg/returnVisitList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
                utilService.requestData($scope);
            }
        })
    };
    //点击回访按钮弹出弹框
    $scope.returnVisit = function (msg) {
        $scope.orderNo = msg;
        $http.get($scope.baseURL+"bg/getOrderByOrderNo?token="+localStorage.token+"&orderNo="+$scope.orderNo).success(function (res) {
            $scope.itemReturn = res.data;
            if (res.state == 0){
                $("#addStaff").css("display","block");
                $(".mark1").css("display","block");
            }
        });
    };
    //引入公用请求链接
    $scope.baseURL = utilService.baseURL();
    //刷新时保留原有的状态
    $scope.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    //刚开始进入界面要显示的数据
    $scope.orderRefer = false;
    $scope.orderList = true;
    if ($scope.orderList){
        $scope.url = $scope.baseURL+"bg/returnVisitList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
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
        $scope.url = $scope.baseURL+"bg/returnVisitList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        sessionStorage.setItem("index",$scope.index);
        $scope.url = $scope.baseURL+"bg/returnVisitList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    });
    //点击具体某个页码
    //就是用事件监听的方法才能给动态创建的方法绑定事件
    $(document).on("click",".current1",function(){
        $(this).addClass("current").siblings().removeClass("current");
        $scope.index = $(this).text();
        sessionStorage.setItem("index",$scope.index);
        $scope.url = $scope.baseURL+"bg/returnVisitList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
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
        if (ns == "" || ns == null || ns == undefined){
            return ""
        }else {
            return time = Y+M+D+h+m;
        }
    }
    //数据转换
    $scope.formatDescription = function (str,way) {
        return utilService.formatDescription(str,way);
    }
}]);