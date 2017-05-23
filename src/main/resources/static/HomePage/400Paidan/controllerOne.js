 var myApp1 = angular.module("First.one",[]);
myApp1.controller("controllerOne",["$scope","$state","$http","utilService",function ($scope,$state,$http,utilService) {
    //引入公用请求链接
    $scope.baseURL = utilService.baseURL();
    var lon = document.getElementById("lon");
    var lati = document.getElementById("lati");
    lon.innerHTML = "输入坐标";
    lati.innerHTML = "输入坐标";
    var radio_carType = $(".radio_carType");
    var radioSex = $(".radioSex");
    var radioOrderType = $(".radioOrderType");
    $scope.problemDescription = "点击选择故障类型";
    $scope.remark = "填写备注";
    $scope.tel = 13842889646;
    $scope.address = "萍水西街";
    $scope.userName = "123456";
    $scope.lon = "";
    $scope.lati = "";
    $scope.appointmentTime = "";
    $(function () {
        $( "input[name='act_start_time'],input[name='act_stop_time']" ).datetimepicker();
        $(".radio").on("click",function () {
            $(this).addClass("on").parent().siblings().find(".radio").removeClass("on");
        });
        $(".radioOrderType").on("click",function () {
            $(this).addClass("on").parent().siblings().find(".radio").removeClass("on");
            if ($(this).parent().text() == "预约订单"){
                $("#hiddle400").css("display","block")
            }else {
                $("#hiddle400").css("display","none")
            }
        });
        $(".span400_1_1").on("click",function () {
            $(this).addClass("on").siblings().removeClass("on");
        });
        $(document).on("click","#ul li",function () {
            $(this).find(".checkbox").toggleClass("on");
        })
    });

    $scope.markFlag = false;
    $scope.markFlagDault = false;
    $scope.addDault = function () {
        $scope.markFlag = true;
        $scope.markFlagDault = true;
    }
    $scope.affirm = function () {
        var classOn = $("#ul li").find(".on").next();
        var liSpan = $(".checkbox");
        var classOnInnerHtml =[];
        var classNumber = [];
        for (var i = 0;i < classOn.length;i++){
            classOnInnerHtml.push(classOn[i].innerHTML);
        }
        for(var i = 0;i < liSpan.length;i++){
            if (liSpan[i].classList.length == 2){
                classNumber.push(i+1);
            }
        }
        var numberQianduan = classOnInnerHtml.join("/")
        var numberHoutai = classNumber.join("-")
        $scope.problemDescription = numberQianduan;
        $scope.problemDescriptionId = numberHoutai;
        $scope.markFlag = false;
        $scope.markFlagDault = false;
    };
    $scope.abolish = function () {
        $scope.markFlag = false;
        $scope.markFlagDault = false;
    };
    $scope.close = function () {
        $scope.markFlag = false;
        $scope.markFlagDault = false;
    };
    $(function () {
        // 1.监听输入文字时间
        var $input = $('#address');
        $input.on('input porpertychange', function () {
            local.search($input.val());
        });
    });
    // 百度地图API功能
    var map = new BMap.Map("l-map");
    map.centerAndZoom(new BMap.Point(120.107114, 30.30704), 11);
    var options = {
        onSearchComplete: function(results){
            if (local.getStatus() == BMAP_STATUS_SUCCESS){
                // a用于存放包含查询信息的对象
                console.log(results);
                var a = [];
                // 返回当前页的结果数
                for (var i = 0; i < results.getCurrentNumPois(); i ++){
                    var item = {
                        name : results.getPoi(i).title,
                        place : results.getPoi(i).address,
                        point : results.getPoi(i).point
                    };
                    a.push(item);
                }
                $('#ulll').empty();
                for(var i=0; i< a.length; i++) {
                    var $li = $('<li></li>');
                    var item = '<p>'+a[i].name+'</p><span>'+a[i].place+'</span>';
                    $li.html(item);
                    // 把信息对象 JSON 字符串化，添加给自定义对象
                    $li.attr('data-info',JSON.stringify(a[i]));
                    $('#ulll').append($li);
                };

                $('#ulll li').on('click', function () {
                    var $info = JSON.parse($(this).attr('data-info'));
                    $scope.lon = String($info.point.lng);
                    $scope.lati = String($info.point.lat);
                    lon.innerHTML = $scope.lon;
                    lati.innerHTML = $scope.lati;
                    $('#ulll').css("display","none")
                });

            }
        }
    };
    var local = new BMap.LocalSearch(map, options);
    $scope.sendAffirm = function () {
        var stringTime = $("#appointmentTime").val();
        var timestamp2 = Date.parse(new Date(stringTime));
        $scope.radio_carType = typeNumber(radio_carType);
        $scope.radioSex = typeNumber(radioSex);
        $scope.radioOrderType = typeNumber(radioOrderType);
        $scope.tel = $("#tel").val();
        $scope.address = $("#address").val();
        $scope.userName = $("#userName").val();
        $scope.lon = $("#lon").text();
        $scope.appointmentTime = timestamp2;
        $scope.lati = $("#lati").text();
        if ($scope.radioOrderType == 0){
            var data = {
                token:localStorage.token,
                type:$scope.radioOrderType,
                nickname:$scope.userName,
                sex:$scope.radioSex,
                tel:$scope.tel,
                faultId:$scope.problemDescriptionId,
                faultDescription:$scope.problemDescription,
                lon:$scope.lon,
                lati:$scope.lati,
                loc:$scope.address,
                carType:$scope.radio_carType
            };
        }else{
            var data = {
                token:localStorage.token,
                type:$scope.radioOrderType,
                nickname:$scope.userName,
                sex:$scope.radioSex,
                tel:$scope.tel,
                faultId:$scope.problemDescriptionId,
                faultDescription:$scope.problemDescription,
                appointmentTime:$scope.appointmentTime,
                lon:$scope.lon,
                lati:$scope.lati,
                loc:$scope.address,
                carType:$scope.radio_carType
            };
        }
    $.post($scope.baseURL+"bg/telPlaceOrder",data,function (res) {
        if (res.state == 0){
            alert(res.message);
        }
    })
    };
    $scope.sendDisabble = function () {
        alert("取消发送，将要重置");
    };
    //获取单选框的下标
    function typeNumber(obj) {
        for (var i = 0;i < obj.length;i++){
            if (obj[i].classList.length == 3){
                return i;
            }
        }
    }
}]);
