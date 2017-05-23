
angular.module('starter.services', [])
    .factory('utilService',["$http",function($http) {
        // var baseURL = "http://192.168.3.108:8080/qcwy/";
        function requestData(_scope) {
            $http.get(_scope.url).
            success(function (res) {
                console.log(res);
                //总页数
                _scope.totalPage = res.data.pages;
                //页码数组例如[1,2,3,4,5,6,7,8]
                _scope.itemPage = res.data.navigatepageNums;
                //数据数组
                _scope.itemData = res.data.list;
                // // console.log(_scope.itemData);
                // sessionStorage.setItem("itemData",_scope.itemData);
                //是否为第一页
                _scope.firstPage = res.data.isFirstPage;
                //是否为最后一页
                _scope.lastPage = res.data.isLastPage;
                //上一页
                _scope.upPage = res.data.prePage;
                //下一页
                _scope.downPge = res.data.nextPage;
                //循环创建的时候先把上次的页码数组给清空
                $(".ep-pages").empty();
                var arr = _scope.itemPage;
                var pages = document.getElementsByClassName("current1");
                if (_scope.firstPage){
                    $("#pageRightDown").css("display","none");
                }else{
                    $("#pageRightDown").css("display","block");
                }
                if (_scope.lastPage){
                    $("#pageLftUp").css("display","none");
                }else{
                    $("#pageLftUp").css("display","block");
                }
                if (_scope.lastPage && _scope.lastPage){
                    $("#pageLftUp").css("display","none");
                    $("#pageLftUp").css("display","none")
                }

                for (var i = 0; i< arr.length;i++){
                    if (arr[i] == _scope.index){
                        $(".ep-pages").append("<a href='javascript:void(0)' class='current1 current'>"+arr[i]+"</a>")
                    }else {
                        $(".ep-pages").append("<a href='javascript:void(0)' class='current1'>"+arr[i]+"</a>")
                    }
                }
            })
        };
        function referOrder(_scope) {
            _scope.itemData = [];
            $http.get(_scope.url).
            success(function (res) {
                console.log(res);
                //数据数组
                _scope.itemData = res.data;
            })
        };
        //零件转化
        function getWxFaultComponent(way) {
            switch (way){
                case 1:
                    return "补胎";
                    break;
                case 2:
                    return "轮毂故障";
                    break;
                case 3:
                    return "轴承、车轴故障";
                    break;
                case 4:
                    return "换内/外胎";
                    break;
                case 5:
                    return "刹把/线故障";
                    break;
                case 6:
                    return "刹车柄/线故障";
                    break;
                case 7:
                    return "刹车鼓故障";
                    break;
                case 8:
                    return "碟刹故障";
                    break;
                case 9:
                    return "转把故障";
                    break;
                case 10:
                    return "灯故障";
                    break;
                case 11:
                    return "线路故障";
                    break;
                case 12:
                    return "结构部件故障";
                    break;
                case 13:
                    return "控制器故障";
                    break;
                case 14:
                    return "喇叭故障";
                    break;
                case 15:
                    return "锁故障";
                    break;
                case 16:
                    return "购买电池";
                    break;
                case 17:
                    return "购买充电器";
                    break;
                case 18:
                    return "塑件损坏";
                    break;
                case 19:
                    return "两轮电机故障";
                    break;
                case 20:
                    return "三轮电机故障";
                    break;
                case 21:
                    return "后桥故障";
                    break;
                default:
                    return "其他"
            }
        };
        function getWxFaultDescription(way) {
            switch (way){
                case 1:
                    return "车不走/有电不走";
                    break;
                case 2:
                    return "车轮故障";
                    break;
                case 3:
                    return "刹车系统故障";
                    break;
                case 4:
                    return "电路故障";
                    break;
                case 5:
                    return "锁故障";
                    break;
                case 6:
                    return "购/换电池";
                    break;
                case 7:
                    return "购/换充电器";
                    break;
                case 8:
                    return "塑件损坏";
                    break;
                case 9:
                    return "车辆异响";
                    break
                default:
                    return "其他"
            }
        }

        return {
            requestData:function (_scope) {
                 requestData(_scope);
            },
            baseURL:function () {
                var baseURL = "http://wechat.qichengwuyou.com/qcwy/";
                return baseURL
            },
            //故障描述转化
            formatDescription:function (str,way) {
               str = str == undefined?"1":str;
            var defultArry = [];
            var ids = [];
            if (str.length > 1){
               ids = str.split("-");
            }else {
                ids.push(parseInt(str));
            }

            for (var i = 0;i<ids.length;i++){
                var number = Number(ids[i]);
                if (way == 1){
                    defultArry.push( getWxFaultDescription(number))
                }else if (way == 2){
                    defultArry.push(getWxFaultComponent(number)) ;
                }
            }
            return defultArry.join("/")
        },
            //根据订单号查询订单详情
            referOrder:function (_scope) {
                referOrder(_scope)
            },
        }
    }]);