var myApp1 = angular.module("marketManage.partList",[
    'ui.router',
    'starter.services'
]);
myApp1.controller("controllerPartList",["$scope","$http","utilService",function ($scope,$http,utilService) {
    $scope.baseURL = utilService.baseURL();
    $scope.partName = false;
    $(function () {

        $("#selectP2").change(function () {
            var oVale = $(this).find("option:selected").text();
            $(".zhengpinku").text(oVale);
        });
        $("#selectP3").change(function () {
            var oVale = $(this).find("option:selected").text();
            $(".shangjia").text(oVale);
        });
        $(".span400_1_1").on("click",function () {
            $(this).addClass("on").siblings().removeClass("on");
        });
    });
    //点击添加商品获取零件类慈
    $scope.addpart = function () {
        $(".mark1").css("display","block");
        $("#addStaff").css("display","block");
      $http.get($scope.baseURL+"bg/getAllPartModel?token="+localStorage.token).success(function (res) {
          $scope.itemType = res.data;
      })
    };
    //根据零件类型获取获取具体零件名称
    $(document).on("change","#partStandard",function () {
        $scope.classify = $("#partStandard option:selected").val();
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
    $scope.baseURL = utilService.baseURL();
    //刷新页面后  保持上面零件兰的状态
    if (sessionStorage.number == undefined){
        $(".span5"+0).addClass("on").siblings().removeClass("on");
    }else {
        $(".span5"+sessionStorage.number).addClass("on").siblings().removeClass("on");
    }
    //刷新页面的时候保持下面的数据显示保持不变   一开始进入页面的时候默认进去四大部件中的第一页数据 并给calssify  index  赋值
        sessionStorage.classify == undefined? $scope.classify = 1:$scope.classify= sessionStorage.classify;
        $scope.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    //进来现请求数据
    $scope.url = $scope.baseURL+"bg/getPartList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5&classify="+$scope.classify;
    utilService.requestData($scope);
    //点击某个零件栏  获取具体零件的数据
    $(".top_PFl span").on("click",function () {
        $(this).addClass("on").siblings().removeClass("on");
        sessionStorage.setItem("number",$(".top_PFl span").index($(this)));
        $scope.classify = $(".top_PFl span").index($(this))+1;
        sessionStorage.setItem("classify",$scope.classify);
        $scope.url = $scope.baseURL+"bg/getPartList?token="+localStorage.token+"&pageNum=1&pageSize=5&classify="+$scope.classify;
        utilService.requestData($scope);
    });
    //点击下一页
    $("#pageLftUp").on("click",function () {
        $scope.index = $scope.downPge;
        $scope.url = $scope.baseURL+"bg/getPartList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5&classify="+$scope.classify;
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        $scope.url = $scope.baseURL+"bg/getPartList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5&classify="+$scope.classify;
        utilService.requestData($scope);
    });
    //点击具体某个页码
    //就是用事件监听的方法才能给动态创建的方法绑定事件
    $(document).on("click",".current1",function(){
        $(this).addClass("current").siblings().removeClass("current");
        $scope.index = $(this).text();
        sessionStorage.setItem("index",$scope.index);
        $scope.url = $scope.baseURL+"bg/getPartList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=5&classify="+$scope.classify;
        utilService.requestData($scope);
    });
    //点击修改商品
    $scope.hiddle = function (msg) {
        $(".mark1").css("display","block");
        $("#addStaff1").css("display","block");
        $http.get($scope.baseURL+"bg/getPartDetailById?token="+localStorage.token+"&partId="+msg).success(function (res) {
            $scope.itemsData = res.data;
            console.log(res);
            console.log(res.data);
            console.log($scope.itemsData)
        })
    };
    //点击添加商品添加图片
    $(document).on("change","#file",function () {
        showImg("file")
    });
    //点击编辑商品添加图片
    $(document).on("change","#file1",function () {
        showImg("file1")
    });
    //添加商品点击确定后发送数据
    $scope.sendAddImg = function () {
        readAsDataURL()
    }
    //把图片转化成base64显示在前端图片
    function showImg(way) {
        //检验是否为图像文件
        var file = document.getElementById(way).files[0];
        if (!/image\/\w+/.test(file.type)) {
            alert("看清楚，这个需要图片！");
            return false;
        }
        var reader = new FileReader();
        //将文件以Data URL形式读入页面
        reader.readAsDataURL(file);
        reader.onload = function (e) {
            var img1 = document.getElementsByClassName("img1");
            //显示文件
            img1[0].src = this.result;
        }
    }
    $scope.hiddleImg = function () {
        $(".mark1").css("display","none");
        $("#addStaff").css("display","none");
        $("#addStaff1").css("display","none")
    };
    //4数据传给后台
    function readAsDataURL() {
        var form=document.getElementById("form");
        var formData=new FormData(form);
        formData.append('token',localStorage.token);
        formData.append('partId', $("#count option:selected").val());
        formData.append('model', $("#partType").val());
        formData.append('unit', $("#degree").val());
        formData.append('price', $(".shangjia").val());
        formData.append('priceNew',$("#degreePrace").val());
        formData.append('priceOld', $("#degreePrace1").val());
        formData.append('isGuaratees', $("#degreePrace2 option:selected").val());
        formData.append('guaranteesLimit', $("#degreePrace3").val());
        formData.append('remark', $("#beizhu").val());
        $.ajax({
            url:$scope.baseURL+"bg/addPart",
            type:"POST",
            data:formData,
            contentType: false,
            processData: false,
            success:function (res) {
                $(".mark1").css("display","none");
                $("#addStaff").css("display","none")
            },
            error:function () {
                alert("请求超时");
            }
        })
    }
    //修改商品数据
    $scope.editeAffirm = function () {
        var form=document.getElementById("form1");
        var formData=new FormData(form);
        formData.append('token', localStorage.token);
        formData.append('partId', $("#partStandard1").val());
        formData.append('partNo', $scope.classify);
        formData.append('model', $("#partType1").val());
        formData.append('unit', $("#degree1").val());
        formData.append('price', $("#shangjia").val());
        formData.append('priceNew',$("#degreePrace_1").val());
        formData.append('priceOld', $("#degreePrace1_1").val());
        formData.append('isGuaratees', $("#degreePrace2_1 option:selected").val());
        formData.append('guaranteesLimit', $("#degreePrace3_1").val());
        formData.append('remark', $("#beizhu1").val());
        $.ajax({
            url:$scope.baseURL+"bg/updatePart",
            type:"POST",
            data:formData,
            contentType: false,
            processData: false,
            success:function (res) {
                $(".mark1").css("display","none");
                $("#addStaff1").css("display","none")
            },
            error:function () {
                alert("请求超时！！！");
            }
        })
    }
    }]);

