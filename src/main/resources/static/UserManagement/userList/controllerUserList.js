var app = angular.module("userManage.userList",[]);
app.controller("controllerUserList",["$scope","$http","utilService","$window","$state",function ($scope,$http,utilService,$window,$state) {
    $(".selectName1").change(function () {
        var oVale = $(this).find("option:selected").text();
        $("#selectStaffName").text(oVale);
    });
    $(document).on("click",".radio",function () {
        $(this).addClass("on").parent().siblings().find(".radio").removeClass("on");
    })
    $scope.baseURL = utilService.baseURL();
    $scope.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    $scope.orderRefer = false;
    $scope.orderList = true;
    if ($scope.orderList){
        $scope.url = $scope.baseURL+"bg/getWxUsers?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
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
        $scope.url = $scope.baseURL+"bg/getWxUsers?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        sessionStorage.setItem("index",$scope.index);
        $scope.url = $scope.baseURL+"bg/getWxUsers?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击具体某个页码
    //就是用事件监听的方法才能给动态创建的方法绑定事件
    $(document).on("click",".current1",function(){
        $(this).addClass("current").siblings().removeClass("current");
        $scope.index = $(this).text();
        sessionStorage.setItem("index",$scope.index);
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
        return time = Y+M+D+h+m;
    }
    //修改弹窗
    $scope.markFlaguserList = false;
    $scope.markFlaguserListadd = false;
    $scope.amend =function (openid,tel,name) {
        $scope.openId = openid;
        $scope.tel = tel;
        $scope.name1 = name;
        $scope.markFlaguserList = true;
        $scope.markFlaguserListadd = true;
    };
    //修改完成后确定按钮
    $scope.amendAffirm = function () {
        var radio = $(".radio");
        for (var i = 0; i< radio.length;i++){
            if (radio[i].classList.length == 2){
                $scope.sex = i+1;
                alert($scope.sex)
            }
        }
        $scope.openId = $("#openid").val();
        $scope.tel = $("#tel").val();
        $.post($scope.baseURL+"bg/updateWxUser",{
            token:localStorage.token,
            openid:$scope.openId,
            nickname:$scope.name1,
            sex:$scope.sex,
            tel:$scope.tel
        },function (res) {
            if (res.state == 0){
                $scope.markFlaguserList = false;
                $scope.markFlaguserListadd = false;
            }
        });
    };
    //修改完成后的取消按钮
    $scope.amenddisable = function () {
        $scope.markFlaguserList = false;
        $scope.markFlaguserListadd = false;
    }
    //禁用
    $scope.disabled = function (msg) {
        $http.get($scope.baseURL+"bg/block?token="+localStorage.token+"&openid="+msg).success(function (res) {
            if (res.state == 0){
                $scope.url = $scope.baseURL+"bg/getWxUsers?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5";
                utilService.requestData($scope);
            }else{
                alert(res.message);
            }
        })
    }
    //时间戳转换
    $scope.getLocalTime = function(ns) {
        var date = new Date(ns);
        Y = date.getFullYear() + '-';
        M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        D = date.getDate() + ' ';
        h = date.getHours() + ':';
        m = date.getMinutes()
        if (ns == null || ns == undefined || ns == ""){
            return "该用户还没有修过！！！"
        }else{
            return time = Y+M+D+h+m;
        }
    }
}]);