var app = angular.module("marketManage.historyOrder",[
    'ui.router',
    'starter.services'
]);
app.controller("controllerhistoryOrder",["$scope","$http","utilService",function ($scope,$http,utilService) {
    $scope.baseURL = utilService.baseURL();
    $(".selectName1").change(function () {
        var oVale = $(this).find("option:selected").text();
        $("#selectStaffName").text(oVale);
    });
    $scope.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    //进来现请求数据
    $scope.orderRefer = false;
    $scope.orderList = true;
    if ($scope.orderList){
        $scope.url = $scope.baseURL+"bg/getHistoryOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
        utilService.requestData($scope);
    }
    $scope.referOrder = function () {
        alert(1111);
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
        sessionStorage.setItem("number",$(".top_PFl span").index($(this)));
        $scope.classify = $(".top_PFl span").index($(this))+1;
        sessionStorage.setItem("classify",$scope.classify);
        $scope.url = $scope.baseURL+"bg/getHistoryOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击下一页
    $("#pageLftUp").on("click",function () {
        $scope.index = $scope.downPge;
        $scope.url = $scope.baseURL+"bg/getHistoryOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        $scope.url = $scope.baseURL+"bg/getHistoryOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击具体某个页码
    $scope.itemData = sessionStorage.itemData;
    //就是用事件监听的方法才能给动态创建的方法绑定事件
    $(document).on("click",".current1",function(){
        $(this).addClass("current").siblings().removeClass("current");
        $scope.index = $(this).text();
        $scope.url = $scope.baseURL+"bg/getHistoryOrders?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //问题类型转化
    $scope.formatDescription = function (str,way) {
       return utilService.formatDescription(str,way)
    };
}]);
