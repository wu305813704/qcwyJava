var myApp = angular.module("businessManage.staffList",[]);
myApp.controller("controllerStaffList",["$scope","$http","utilService",function ($scope,$http,utilService) {
    $scope.baseURL = utilService.baseURL();
    //去掉字符串左右两端的空格
    function trim(str){
        return str.replace(/(^\s*)|(\s*$)/g, "");
    }
    var arr = document.getElementsByClassName("top_pTypeIn");
    //判断不同状态显示的颜色
    for(var i = 0; i< arr.length;i++){
        if (trim(arr[i].innerHTML) == "离职"){
            arr[i].style.color = "red";
        }else if (trim(arr[i].innerHTML) == "在职"){
            arr[i].style.color = "";
        }
    }
    $scope.userNo = '';
    $scope.userName = "";
    $scope.userTel = "" ;
    $scope.passWord = "";
    $scope.sex = "";
    $scope.roleId = "";
    $(function () {
        $(".radio").on("click",function () {
            $(this).addClass("on").parent().siblings().find(".radio").removeClass("on");
        });
        $("#selectROle1").change(function () {
            var oVale = $(this).find("option:selected").text();
            $(".selectROle").text(oVale);
        });
        $("#typeSelect").change(function () {
            var oVale = $(this).find("option:selected").text();
            $(".typeSelectValue").text(oVale);
        });
        $(".selectName1").change(function () {
            var oVale = $(this).find("option:selected").text();
            $("#selectStaffName").text(oVale);
        });
        $(".span400_1_1").on("click",function () {
            $(this).addClass("on").siblings().removeClass("on");
        });
        $(".addStaff").on("click",function () {
            $(".mark1").css("display","block");
            $("#addStaff").css("display","block");
            $http.get($scope.baseURL+"bg/getAllRole?token="+localStorage.token).success(function (res) {
                $scope.itemRoleList = res.data;
            })
        });
        $(".addStaff_TopR").on("click",function () {
            $(".mark1").css("display","none");
            $("#addStaff").css("display","none");
            $("#orderDescription").css("display","none");
        });
        $(".addStaff_Btomqueren").on("click",function () {
            var radio = $(".radio");
            for (var i = 0; i<radio.length;i++){
                if (radio[i].classList.length == 2){
                    $scope.sex = i+1
                }
            }
            $scope.roleId = $(".selectROle").text();
            $.post($scope.baseURL+"bg/addBgUser",{
                token:localStorage.token,
                username:$scope.userNo,
                pwd:$scope.passWord,
                name:$scope.userName,
                sex:$scope.sex,
                tel:$scope.userTel,
                roleId:$("#selectROle1 option:selected").val()
            },function (res) {
                if (res.state == 0){
                    alert(res.message)
                    $(".mark1").css("display","none");
                    $("#addStaff").css("display","none");
                }
            })
        });
        $(".addStaff_Btomreset").on("click",function () {
            $(".mark1").css("display","none");
            $("#addStaff").css("display","none");
        });
    });
    $scope.token = "aaa";
    $scope.index == undefined? $scope.index = 1:$scope.index= sessionStorage.index;
    $scope.url = $scope.baseURL+"bg/getBgUserList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
    utilService.requestData($scope);
    //一进来先请求一次数据  参数1：第一页
    //点击下一页
    $("#pageLftUp").on("click",function () {
        $scope.index = $scope.downPge;
        $scope.url = $scope.baseURL+"bg/getBgUserList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
        utilService.requestData($scope);
    });
    //点击上一页
    $("#pageRightDown").on("click",function () {
        $scope.index = $scope.upPage;
        $scope.url = $scope.baseURL+"bg/getBgUserList?token="+localStorage.token+"&pageNum="+$scope.index+"&pageSize=9";
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
    //点击某一页进行变色  并请求当前页的数据
    function setListeners() {
        $(".current1").on("click",function () {
            $(this).addClass("current").siblings().removeClass("current");
            $scope.index = $(this).text();
            if ($scope.classify == undefined){
                requestData($scope.index,1);
            }else {
                requestData($scope.index,$scope.classify);
            }
        })
    }
    //请求数据  并判断上下页的显示和隐藏
    //参数说明   token：获取数据的唯一标识    pageNumber：当前页    pageSize：每一页显示几条数据?token=aaa&pageNum=1&pageSize=1
    function requestData(index) {
        $http.get($scope.baseURL+"bg/getBgUserList?token="+localStorage.token+"&pageNum="+index+"&pageSize=10").
        success(function (res) {
            console.log(res);
            //总页数
            $scope.totalPage = res.data.pages;
            //页码数组例如[1,2,3,4,5,6,7,8]
            $scope.itemPage = res.data.navigatepageNums;
            //数据数组
            $scope.itemData = res.data.list;
            //是否为第一页
            $scope.firstPage = res.data.isFirstPage;
            //是否为最后一页
            $scope.lastPage = res.data.isLastPage;
            //上一页
            $scope.upPage = res.data.prePage;
            //下一页
            $scope.downPge = res.data.nextPage;
            //循环创建的时候先把上次的页码数组给清空
            $(".ep-pages").empty();
            var arr = $scope.itemPage;
            //var pages = document.getElementsByClassName("current1");
            if ($scope.firstPage){
                $("#pageRightDown").css("display","none");
            }else{
                $("#pageRightDown").css("display","block");
            }
            if ($scope.lastPage){
                $("#pageLftUp").css("display","none");
            }else{
                $("#pageLftUp").css("display","block");
            }
            for (var i = 0; i< arr.length;i++){
                if (arr[i] == index){
                    $(".ep-pages").append("<a href='javascript:void(0)' class='current1 current'>"+arr[i]+"</a>")
                }else {
                    $(".ep-pages").append("<a href='javascript:void(0)' class='current1'>"+arr[i]+"</a>")
                }
            }
            setListeners();
        })
    }
    $scope.orderDescription = function (msg) {
        $(".mark1").css("display","block");
        $("#orderDescription").css("display","block");
        $scope.userName = msg;
    };
    $scope.amendStaff = function () {
        $scope.staffTel = $("#staffTel").val();
        $scope.staffSex = $("#staffSex option:selected").val();
        $scope.staffType = $("#staffType option:selected").val();
        $scope.staffName = $("#staffName").val();
        $http.get($scope.baseURL+"bg/updateBgUser?token="+localStorage.token+"&userNo="+$scope.userName+"&name="+$scope.staffName+"&sex="+$scope.staffSex+"&tel="+$scope.staffTel+"&state="+$scope.staffType).success(function (res) {
            if (res.state == 0){
                $(".mark1").css("display","none");
                $("#orderDescription").css("display","none");
                $scope.url = $scope.baseURL+"bg/getBgUserList?token="+localStorage.token+"&pageNum=1&pageSize=9";
                utilService.requestData($scope);
            }
        })
    };
    $scope.resetStaff = function () {
        $(".mark1").css("display","none");
        $("#orderDescription").css("display","none");
    };
/************************弹窗****************************/
    //转码多个角色名称数组
    $scope.typeNamerole = function (msg) {
        var str = ''
        var arr = [];
            for (var i = 0;i <msg.length;i++){
                arr.push(msg[i].role_name);
            }
            str = arr.join("/");
        return str;
        }
}]);
