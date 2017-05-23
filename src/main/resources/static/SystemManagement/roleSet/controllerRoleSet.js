var app = angular.module("systemManage.roleSet",[]);
app.controller("controllerRoleSet",["$scope","$http","$state","utilService",function ($scope,$http,$state,utilService) {
    $scope.baseURL = utilService.baseURL();
    $(".selectName1").change(function () {
        var oVale = $(this).find("option:selected").text();
        $("#selectStaffName").text(oVale);
    });
    //添加用户
    $scope.markFlagIn1 = false;
    $scope.markFlag = false;
    $scope.markFlagIn = false;
    $scope.addStaff = function () {
        $scope.markFlag = true;
        $scope.markFlagIn = true;
        $http.get($scope.baseURL+"bg/getAllMenu?token="+localStorage.token).success(function (res) {
            $scope.addItems = res.data
            console.log($scope.addItems)
        })
    };
    //选择角色权限
    $(document).on("click",".roleSetquanxianFirstImg1",function () {
        $(this).toggleClass("on");
    });
    $(".span400_1_1").on("click",function () {
        $(this).addClass("on").siblings().removeClass("on");
    });
    $scope.hiddle = function () {
        $scope.markFlag = false;
        $scope.markFlagIn = false;
    };
    $scope.send = function () {
        var arr = [];
        var rolesetadd = document.getElementsByClassName("rolesetadd");
        var roleSetquanxianFirstImg1 = document.getElementsByClassName("roleSetquanxianFirstText");
        var roleSetquanxianFirstTextId = document.getElementsByClassName("roleSetquanxianFirstTextId")
        for (var i = 0;i < roleSetquanxianFirstImg1.length;i++){
            if (document.getElementsByClassName("roleSetquanxianFirstImg1")[i].classList.length == 2){
                alert(i);
                var id = parseInt(roleSetquanxianFirstTextId[i].innerHTML)
                arr.push(id);
            }
        }
        var str = arr.join("-");
        console.log(arr);
        console.log(str);
        console.log($scope.items);
        var data = {
            token:localStorage.token,
            roleName:$(".addStaff_BottomSpan1_input").val(),
            menuIds:str
        };
        console.log(data);
        $.ajax({
            type: "POST",
            url: $scope.baseURL+"bg/addRole",
            data:data,
            cache: false,
            traditional: true,
            success: function(res){
                alert(res.message);
                alert("OK");
            }
        });
        $scope.markFlag = false;
        $scope.markFlagIn = false;
    }

    //刷新时保留原有的状态
    //刚开始进入界面要显示的数据
    $http.get($scope.baseURL+"bg/getAllRole?token="+localStorage.token).success(function (res) {
        alert(res.message);
        var items = res.data;
        for (var i = 0;i <items.length;i++){
            var arr = items[i].menus
            console.log(arr)
            var arr1 = [];
            for (var j = 0;j < arr.length;j++){
                arr1.push(arr[j].name);
            }
            var str = arr1.join(",");
            items[i].str = str;
        }
        console.log(items)
        $scope.items = items;
    });
    //时间戳转换
    $scope.getLocalTime = function(ns) {
        var date = new Date(ns);
        Y = date.getFullYear() + '-';
        M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        D = date.getDate() + ' ';
        h = date.getHours() + ':';
        m = date.getMinutes();
        if (ns == "" || ns == null || ns == undefined){
            return ""
        }else {
            return time = Y+M+D+h+m;
        }

    };
    $scope.referOrder = function () {
        var referOrder = $("#seletNameValue").val();
        console.log(referOrder);
        alert("一定要查出来奥")  ;
        $scope.url ="http://192.168.3.108:8080/qcwy/bg/getOrderByOrderNo?token=aaa&orderNo="+referOrder;
        utilService.referOrder($scope);
    };
    //数据转换
    $scope.formatDescription = function (str,way) {
        return utilService.formatDescription(str,way);
    };
    //修改数据
    $scope.edit =function (msg) {
        $scope.role = msg;
        $scope.markFlagIn1 = true;
        $scope.markFlag = true;
        $http.get("http://192.168.3.108:8080/qcwy/bg/getAllMenu?token=aaa").success(function (res) {
            $scope.editItems = res.data
            console.log($scope.editItems)
        })
    }

}]);