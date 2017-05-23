var app = angular.module("basicData.basicDataStock",[]);
app.controller("controllerBasicDataStock",["$scope","$http","utilService",function ($scope,$http,utilService) {
    $scope.partName = false;
    $scope.baseURL = utilService.baseURL();
    //存权限token
    //sessionStorage.setItem("token","de0285ccce43632ac8923a8fc91451b1");?token=aaa&type=0&pageNum=1&pageSize=5
    //刷新页面后  保持上面零件兰的状态
    if (sessionStorage.number == undefined){
        $(".span5"+0).addClass("on").siblings().removeClass("on");
    }else {
        $(".span5"+sessionStorage.number).addClass("on").siblings().removeClass("on");
    }
    //刷新页面的时候保持下面的数据显示保持不变   一开始进入页面的时候默认进去四大部件中的第一页数据 并给calssify  index  赋值
    sessionStorage.classify == undefined? $scope.classify = 1:$scope.classify= sessionStorage.classify;
    sessionStorage.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    sessionStorage.number == undefined ? $scope.entrepotType = 0 :$scope.entrepotType = sessionStorage.number;
    //进来现请求数据
    alert($scope.index);
    $scope.url = $scope.baseURL+"bg/getWarehouse?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5&type="+$scope.classify;
    utilService.requestData($scope);
    //点击某个零件栏  获取具体零件的数据
    $(".top_PFl span").on("click",function () {
        $(this).addClass("on").siblings().removeClass("on");
        sessionStorage.setItem("number",$(".top_PFl span").index($(this)));
        $scope.classify = $(".top_PFl span").index($(this));
        sessionStorage.setItem("classify",$scope.classify);
        $scope.url = $scope.baseURL+"bg/getWarehouse?token="+localStorage.token+"&pageNum="+1+"&pageSize=5&type="+$scope.classify;
        utilService.requestData($scope);
    });
    //点击下一页
    $("#pageLftUp").on("click",function () {
        $scope.index = $scope.downPge;
        $scope.url = $scope.baseURL+"bg/getWarehouse?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5&type="+$scope.classify;
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        $scope.url = $scope.baseURL+"bg/getWarehouse?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5&type="+$scope.classify;
        utilService.requestData($scope);
    });
    //点击具体某个页码
    // $scope.itemData = sessionStorage.itemData;
    // // console.log(sessionStorage.itemData);
    // // console.log($scope.itemData+"12315");
    //就是用事件监听的方法才能给动态创建的方法绑定事件
    $(document).on("click",".current1",function(){
        $(this).addClass("current").siblings().removeClass("current");
        $scope.index = $(this).text();
        $scope.url = $scope.baseURL+"bg/getWarehouse?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5&type="+$scope.classify;
        utilService.requestData($scope);
    });
    $("#selectP1").change(function () {
        var oVale = $(this).find("option:selected").text();
        $(".zhengpinku").text(oVale);
    });
    $("#selectP2").change(function () {
        var oVale = $(this).find("option:selected").text();
        $(".shangjia").text(oVale);
    });

    $(".span400_1_1").on("click",function () {
        $(this).addClass("on").siblings().removeClass("on");
    });
    $(".addStaff_TopR").on("click",function () {
        $(".mark1").css("display","none");
        $("#addStaff").css("display","none");
    });


    //点击添加商品获取零件类慈
    $scope.addpart = function () {
        // $scope.addPart = true;
        // $scope.markflag = true;
        $(".mark1").css("display","block");
        $("#addStaff").css("display","block");
        $http.get($scope.baseURL+"bg/getAllPartModel?token="+localStorage.token).success(function (res) {
            $scope.itemType = res.data;
        })
    };
    $scope.addSendAffrim = function () {
         $scope.partDetailId = $("#degree option:selected").val();
        $scope.tpye = $("#partName option:selected").val();
        $http.get($scope.baseURL+"bg/addPartToWarehouse?token="+localStorage.token+"&type="+$scope.tpye+"&partDetailId="+$scope.partDetailId+"&count="+$("#count").val()).success(function (res) {
            if (res.state == 0){
                $(".mark1").css("display","none");
                $("#addStaff").css("display","none");
                $scope.url = $scope.baseURL+"bg/getWarehouse?token="+localStorage.token+"&pageNum=1&pageSize=5&type="+$scope.classify;
                utilService.requestData($scope);
            }
        })

    };
    //根据零件类型获取获取具体零件名称
    $(document).on("change","#partType",function () {
        //alert($("#partStandard option:selected").val())
        $scope.classify = $("#partType option:selected").val();
        $http.get($scope.baseURL+"bg/getPartByClassify?token="+localStorage.token+"&classify="+$scope.classify).success(function (res) {
            if (res.state == 0){
                $scope.partName = true;
                $scope.itemPartNameList = res.data;
            }else {
                $scope.partName = false;
                alert("请求失败");
            }

        })
    });
    $(document).on("change","#partType1",function () {
        //alert($("#partStandard option:selected").val())
        $scope.classify = $("#partType1 option:selected").val();
        $http.get($scope.baseURL+"bg/getPartByClassify?token="+localStorage.token+"&classify="+$scope.classify).success(function (res) {
            if (res.state == 0){
                $scope.partName = true;
                $scope.itemPartNameList = res.data;
            }else {
                $scope.partName = false;
                alert("请求失败");
            }
        })
    });

    //编辑商品弹框
    $scope.hiddle = function (msg,name1,modul) {
        $scope.entrepotType = msg;
        $scope.partNAME = name1;
        $scope.modul = modul;
        $(".mark1").css("display","block");
        $("#addStaff1").css("display","block")
    }
    $scope.sendAffrim = function () {
        $http.get($scope.baseURL+"bg/addPartToWarehouse?token="+localStorage.token+"&type="+$scope.entrepotType+"&partDetailId="+$scope.entrepotType+"&count="+$("#count").val()).success(function (res) {
            if (res.state == 0){
                $(".mark1").css("display","none");
                $("#addStaff").css("display","none")
            }
        })
    }
    $scope.sendReset = function () {
        $(".mark1").css("display","none");
        $("#addStaff").css("display","none")
    }
    $scope.sendAffrim1 = function () {
        $http.get($scope.baseURL+"bg/addPartToWarehouse?token="+localStorage.token+"&type="+$scope.entrepotType+"&partDetailId="+$scope.entrepotType+"&count="+$("#count1").val()).success(function (res) {
            if (res.state == 0){
                $(".mark1").css("display","none");
                $("#addStaff1").css("display","none")
            }
        })
    }
    $scope.sendReset1 = function () {
        $(".mark1").css("display","none");
        $("#addStaff1").css("display","none")
    }
}]);

