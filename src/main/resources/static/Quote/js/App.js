
function getHash() {
    var url = location.hash.substr(2);
    var aaa = url.split("/");
    var arr = [];
    console.log(aaa);
    var str = "";
    for (var i = 0;i <aaa.length;i++){
      str = "#"+aaa[i]  ;
      console.log(str);
      arr.push(str);
    }
    return arr;
}
var baseURL = 'http://wechat.qichengwuyou.com/qcwy/';
var WS = "ws://wechat.qichengwuyou.com/qcwy/bgWebSocket/";
var paths = getHash()
console.log(paths);
setTimeout(function () {
    if (paths[0] == "#" ){
        $("#First").addClass("on")
        console.log(paths[0]);
    }else{
        $(paths[0]).addClass("on");
        console.log(paths[0]);
    };
},0);

var myApp1 = angular.module("myApp",
            [
                'ui.router',
                "First.one",
                "businessManage.staffList",
                "marketManage.partList",
                "marketManage.repairOder",
                "marketManage.advanceOrder",
                "marketManage.historyOrder",
                "marketManage.confirm",
                "marketManage.historyApplyfor",
                "operation.masterMap",
                "operation.masterRank",
                "operation.areaAddRate",
                "operation.newIncreased",
                "operation.mounthRate",
                "operation.masterDaily",
                "operation.userDaily",
                "operation.manageDaily",
                "userManage.userList",
                "userManage.userManage1",
                "systemManage.passWord1",
                "systemManage.payManage",
                "systemManage.roleSet",
                "systemManage.systemSet",
                "systemManage.systemType",
                "systemManage.weixinPay",
                "basicData.partList1",
                "basicData.basicDataStock",
                "callCenter.allCenter",
                "callCenter.waitOrder",
                "callCenter.overTimeOrder",
                "callCenter.teacherReassignment",
                "callCenter.serviceOrder",
                "callCenter.historyOrder",
                "callCenter.returnVisit",
                "callCenter.returnVisit1",
                "userManage.blackList",
                "detailMaster",
                "detailCompany",
                "detailUser",
                // "URL",
                'starter.services',
                // 'backStage.services.http',
                "basicData.basicDataBuy"
            ]);


myApp1.controller('homeController',function ($scope,$state) {
$scope.btn = function () {
    alert("dianjile");
    $scope.$broadcast("updateLoc",1)
};
    var itemMenu = [];
    var json = {}
    var item = JSON.parse(localStorage.menu);
    // console.log(item);
    for (var i = 0; i < item.length; i++){
        // alert(item[i].id)
        if (item[i].parent_id == 0){
            json.id = item[i].id;
            json.name = item[i].name;
            json.parent_id = item[i].parent_id;
            json.order_id = item[i].order_id;
        }
        console.log(json);
        itemMenu.push(json);
    }
    $scope.itemMenu = JSON.parse(localStorage.menu);
    console.log(localStorage.menu)
    console.log($scope.itemMenu);
    $scope.routerTpye = function (msg) {
        switch (msg){
            case 1:
                $state.go("businessManage.staffList");
                break;
            case 2:
                $state.go("marketManage.partList");
                break;
            case 3:
                $state.go("operation.masterMap");
                sessionStorage.setItem("infoWindowBtn",false)
                alert(sessionStorage.infoWindowBtn);
                break;
            case 4:
                $state.go("userManage.userList");
                break;
            case 5:
                $state.go("systemManage.systemSet");
                break;
            case 6:
                $state.go("basicData.partList1");
                break;
            case 7:
                $state.go("callCenter.allCenter");
                break;
            default:
                return ""
        }
    }
    $scope.routerId = function (msg) {
        switch (msg){
            case 1:
                return "businessManage";
                break;
            case 2:
                return "marketManage";
                break;
            case 3:
                return "operation";
                break;
            case 4:
                return "userManage";
                break;
            case 5:
                return "systemManage";
                break;
            case 6:
                return "basicData";
                break;
            case 7:
                return "callCenter";
                break;
            default:
                return ""
        }
    };
    //实例化webSocket
    alert(sessionStorage.userNo);
    var ws = new WebSocket(WS  + sessionStorage.userNo);
    //开始连接websocket
    ws.onopen = function () {
        alert("onpen");
        heartCheck.start();
    };
    ws.onmessage = function (data) {
        var str = data.data;
        try{
            var obj = JSON.parse(str);
            console.log(obj);
            switch (obj.type){
                case 100:
                    $scope.$broadcast("updateLoc",obj);
                break;
                case 101:
                    var title = "有订单超时，请及时处理";
                    var body = "订单编号："+obj.data.order_no+"已超时，点击派发";
                    var path = "callCenter.overTimeOrder";
                    notify(title,body,path);
                    break;
                case 102:
                    var title = "有新的预约订单，请及时处理";
                    var body = "订单编号："+obj.data.order_no+"，点击派发";
                    var path = "marketManage.advanceOrder";
                    notify(title,body,path);
                    break;
                case 103:
                    var title = "有新的售后订单，请及时处理";
                    var body = "订单编号："+obj.data.order_no+"，点击查看";
                    var path = "marketManage.confirm";
                    notify(title,body,path);
                    break;
            }

        }catch (err){
            console.log(err);
        }

        heartCheck.reset();
    };
    ws.onclose = function () {
        alert("close");
        ws = new WebSocket(WS  +sessionStorage.userNo);
    };
    ws.onerror = function () {
        alert("error");
        ws = new WebSocket(WS +sessionStorage.userNo);
    };
    var heartCheck = {
        timeout: 10000,//60ms
        timeoutObj: null,
        serverTimeoutObj: null,
        reset: function () {
            clearTimeout(this.timeoutObj);
            clearTimeout(this.serverTimeoutObj);
            this.start();
        },
        start: function () {
            var self = this;
            this.timeoutObj = setTimeout(function () {
                ws.send("HeartBeat");
                self.serverTimeoutObj = setTimeout(function () {
                    ws.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
                }, self.timeout)
            }, this.timeout)
        }
    }
    function notify(title,body,path) {
        var options = {
            title: title,
            options: {
                body: body,
                icon: "../Quote/img/home/icon.png",
                lang: 'pt-BR',
                onClick:myOnclick
            }
        };

        function myOnclick() {
            $state.go(path);
        };
        $("#easyNotify").easyNotify(options);
    }

});
myApp1.config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when("", "/First/one");
    $stateProvider
        .state("First", {
            url: '/First',
            templateUrl:'First.html',
            controller:function ($state) {
                var wrap_MFl_p = document.getElementById("wrap_MFl_p");
                wrap_MFl_p.innerHTML = sessionStorage.userName1;
                if (paths[1] == "#" ){
                    $("#one").addClass("on")
                }else{
                    $(paths[1]).addClass("on").parent().siblings().find("a").removeClass("on");
                };
               //$state.go("First.one");
                var index = 1;
                $(".wrap_MFl_ul_top").on("click",function () {
                    index *= -1;
                    $(".ulList").slideToggle("fast",function () {
                        if (index == 1){
                            $(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_xl_pre.png");
                        }else{
                            $(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_shou.png");
                        }
                    })
                });
                $(".ulListOn").on("click",function () {
                    $(this).addClass("on").parent().siblings().find("a").removeClass("on");
                });
            }
        }).state("First.one",{
        url:"/one",
        templateUrl: '400Paidan/one.html',
        controller:"controllerOne",
    }).state("First.two",{
        url:"/two",
        templateUrl:"../CallCenter/waitOrder/waitOrder.html",
        controller:"controllerWaitOrder"
    }).state("First.three",{
        url:"/three",
        templateUrl:"../CallCenter/overTimeOrder/overTimeOrder.html",
        controller:"controllerOverTimeOrder"
    }).state("First.four",{
        url:"/four",
        templateUrl:"../CallCenter/serviceOrder/serviceOrder.html",
        controller:"controllerServiceOrder"
    }).state("First.five",{
        url:"/five",
        templateUrl: '../Operationstatistics/masterMap/masterMap.html',
        controller: "controllerMasterMap"
    }).state("First.six",{
        url:"/six",
        templateUrl: '../marketManagement/partManage/partList.html',
        controller: "controllerPartList"
    }).state("First.seven",{
        url:"/seven",
        templateUrl:"../BasicData/basicDataStock/basicDataStock.html",
        controller:"controllerBasicDataStock"
    }).state("First.eight",{
        url:"/eight",
        templateUrl: '../marketManagement/advanceOrder/advanceOrder.html',
        controller: "controlleradvanceOrder"
    }).state("First.nine",{
        url:"/nine",
        templateUrl:"../CallCenter/allCenter/allCenter.html",
        controller:"controllerAllCenter"
    }).state("businessManage",{
        url:"/businessManage",
        templateUrl: '../BusinessManagement/businessManage.html',
        controller: function ($state) {
            var wrap_MFl_p = document.getElementById("wrap_MFl_p");
            wrap_MFl_p.innerHTML = sessionStorage.userName1;
            if (paths[1] == "#" ){
                $("#one").addClass("on")
            }else{
                $(paths[1]).addClass("on");
            };
            // $state.go("businessManage.staffList");
            var index = 1;
            $(".wrap_MFl_ul_top").on("click",function () {
                index *= -1;
                $(".ulList").slideToggle("fast",function () {
                    if (index == 1){
                        $(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_xl_pre.png");
                    }else{
                        $(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_shou.png");
                    }
                })
            });
            $(".ulListOn").on("click",function () {
                $(this).addClass("on").parent().parent().siblings().find("a").removeClass("on");
            });
        }
    }).state("businessManage.staffList",{
        url:"/staffList",
        templateUrl: '../BusinessManagement/staffList.html',
        controller: "controllerStaffList"

    }).state("marketManage",{
        url:"/marketManage",
        templateUrl: '../MarketManagement/marketManage.html',
        controller: function ($state) {
            var wrap_MFl_p = document.getElementById("wrap_MFl_p");
            wrap_MFl_p.innerHTML = sessionStorage.userName1;
            if (paths[1] == "#" ){
                $("#one").addClass("on")
            }else{
                $(paths[1]).addClass("on").parents(".wrap_MFl_ul").siblings().find("a").removeClass("on");
            };
            var index = 1;
            $(".wrap_MFl_ul_top").on("click",function () {
                $(this).parent().find(".ulList").slideToggle("fast",function () {
                    index *= -1;
                    if (index == 1){
                        $(this).parent().parent().find(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_xl_pre.png");
                    }else{
                        $(this).parent().parent().find(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_shou.png");
                    }
                })
            });
            $(".ulListOn").on("click",function () {
                $(this).addClass("on").parents(".wrap_MFl_ul").siblings().find("a").removeClass("on");
                $(this).addClass("on").parent().siblings().find("a").removeClass("on");
            });
        }
    }).state("marketManage.partList",{
        url:"/partList",
        templateUrl: '../marketManagement/partManage/partList.html',
        controller: "controllerPartList"
    }).state("marketManage.repairOder",{
        url:"/repairOder",
        templateUrl: '../MarketManagement/repairOrder/repairOder.html',
        controller: "controllerRepairOrder"
    }).state("marketManage.advanceOrder",{
            url:"/advanceOrder",
            templateUrl: '../marketManagement/advanceOrder/advanceOrder.html',
            controller: "controlleradvanceOrder"
    }).state("marketManage.historyOrder",{
        url:"/historyOrder",
        templateUrl: '../marketManagement/historyOrder/historyOrder.html',
        controller: "controllerhistoryOrder"
    }).state("marketManage.confirm",{
        url:"/confirm",
        templateUrl: '../marketManagement/confirm/confirm.html',
        controller: "controllerconfirm"
    }).state("marketManage.historyApplyfor",{
        url:"/historyApplyfor",
        templateUrl: '../marketManagement/historyApplyfor/historyApplyfor.html',
        controller: "controllerhistoryApplyfor"
    }).state("operation",{
        url:"/operation",
        templateUrl: '../OperationStatistics/operation.html',
        controller: function ($state) {
            var wrap_MFl_p = document.getElementById("wrap_MFl_p");
            wrap_MFl_p.innerHTML = sessionStorage.userName1;
            if (paths[1] == "#" ){
                $("#one").addClass("on")
            }else{
                $(paths[1]).addClass("on").parents(".wrap_MFl_ul").siblings().find("a").removeClass("on");
                $(paths[1]).addClass("on").parent().siblings().find("a").removeClass("on");
            };
            var index = 1;
            $(".wrap_MFl_ul_top").on("click",function () {
                $(this).parent().find(".ulList").slideToggle("fast",function () {
                    index *= -1;
                    if (index == 1){
                        $(this).parent().parent().find(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_xl_pre.png");
                    }else{
                        $(this).parent().parent().find(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_shou.png");
                    }
                })
            });
            $(".ulListOn").on("click",function () {
                $(this).addClass("on").parents(".wrap_MFl_ul").siblings().find("a").removeClass("on");
                $(this).addClass("on").parent().siblings().find("a").removeClass("on");
            });
        }
    }).state("operation.masterMap",{
        url:"/masterMap",
        templateUrl: '../Operationstatistics/masterMap/masterMap.html',
        controller: "controllerMasterMap"
    }).state("operation.masterRank",{
        url:"/masterRank",
        templateUrl: '../Operationstatistics/masterRank/masterRank.html',
        controller: "controllerMasterRank"
    }).state("operation.areaAddRate",{
        url:"/areaAddRate",
        templateUrl: '../Operationstatistics/areaAddRate/areaAddRate.html',
        controller: "controllerareaAddrate"
    }).state("operation.newIncreased",{
        url:"/newIncreased",
        templateUrl: '../Operationstatistics/NewIncreased/newIncreased.html',
        controller: "controllerNewIncreased"
    }).state("operation.mounthRate",{
        url:"/mounthRate",
        templateUrl: '../Operationstatistics/mounthRate/mounthRate.html',
        controller: "controllerMounthRate"
    }).state("operation.masterDaily",{
        url:"/masterDaily",
        templateUrl: '../Operationstatistics/masterDaily/masterDaily.html',
        controller: "controllerMasterDaily"
    }).state("operation.manageDaily",{
        url:"/manageDaily",
        templateUrl: '../Operationstatistics/manageDaily/manageDaily.html',
        controller: "controllerManageDaily"
    }).state("operation.userDaily",{
        url:"/userDaily",
        templateUrl: '../Operationstatistics/userDaily/userDaily.html',
        controller: "controllerUserDaily"
    }).state("userManage",{
        url:"/userManage",
        templateUrl: '../UserManagement/userManage.html',
        controller: function ($state) {
            var wrap_MFl_p = document.getElementById("wrap_MFl_p");
            wrap_MFl_p.innerHTML = sessionStorage.userName1;
            if (paths[1] == "#" ){
                $("#one").addClass("on")
            }else{
                $(paths[1]).addClass("on").parent().siblings().find("a").removeClass("on");
            };
            var index = 1;
            $(".wrap_MFl_ul_top").on("click",function () {
                $(this).parent().find(".ulList").slideToggle("fast",function () {
                    index *= -1;
                    if (index == 1){
                        $(this).parent().parent().find(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_xl_pre.png");
                    }else{
                        $(this).parent().parent().find(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_shou.png");
                    }
                })
            });
            $(".ulListOn").on("click",function () {
                $(this).addClass("on").parents(".wrap_MFl_ul").siblings().find("a").removeClass("on");
                $(this).addClass("on").parent().siblings().find("a").removeClass("on");
            });
        }
    }).state("userManage.userList",{
        url:"/userList",
        templateUrl: '../UserManagement/userList/userList.html',
        controller: "controllerUserList"
    }).state("userManage.userManage1",{
        url:"/userManage1",
        templateUrl: '../UserManagement/userManage/userManage.html',
        controller: "controllerUserDaily"
    }).state("systemManage",{
        url:"/systemManage",
        templateUrl:"../SystemManagement/systemManage.html",
        controller:function () {
            var wrap_MFl_p = document.getElementById("wrap_MFl_p");
            wrap_MFl_p.innerHTML = sessionStorage.userName1;
            if (paths[1] == "#" ){
                $("#one").addClass("on")
            }else{
                $(paths[1]).addClass("on").parents(".wrap_MFl_ul").siblings().find("a").removeClass("on");
                $(paths[1]).addClass("on").parent().siblings().find("a").removeClass("on");
            };
            var index = 1;
            $(".wrap_MFl_ul_top").on("click",function () {
                $(this).parent().find(".ulList").slideToggle("fast",function () {
                    index *= -1;
                    if (index == 1){
                        $(this).parent().parent().find(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_xl_pre.png");
                    }else{
                        $(this).parent().parent().find(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_shou.png");
                    }
                })
            });
            $(".ulListOn").on("click",function () {
                $(this).addClass("on").parents(".wrap_MFl_ul").siblings().find("a").removeClass("on");
                $(this).addClass("on").parent().siblings().find("a").removeClass("on");
            });
        }
    }).state( "systemManage.passWord1",{
        url:"/passWord1",
        templateUrl: '../SystemManagement/changePassword/changePassword.html',
        controller: "controllerChangePassword"
    }).state("systemManage.payManage",{
        url:"/payManage",
        templateUrl: '../SystemManagement/payManage/payManage.html',
        controller: "controllerPayManage"
    }).state("systemManage.roleSet",{
        url:"/roleSet",
        templateUrl: '../SystemManagement/roleSet/roleSet.html',
        controller: "controllerRoleSet"
    }).state("systemManage.systemSet",{
        url:"/systemSet",
        templateUrl: '../SystemManagement/systemSet/systemSet.html',
        controller: "controllerSystemSet"
    }).state("systemManage.systemType",{
        url:"/systemType",
        templateUrl: '../SystemManagement/systemType/systemType.html',
        controller: "controllerSystemType"
    }).state("systemManage.weixinPay",{
        url:"/weixinPay",
        templateUrl: '../SystemManagement/weixinPay/weixinPay.html',
        controller: "controllerWeixinPay"
    }).state("basicData",{
        url:"/basicData",
        templateUrl:"../BasicData/basicData.html",
        controller:function () {
            var wrap_MFl_p = document.getElementById("wrap_MFl_p");
            wrap_MFl_p.innerHTML = sessionStorage.userName1;
            if (paths[1] == "#" ){
                $("#one").addClass("on")
            }else{
                $(paths[1]).addClass("on").parents(".wrap_MFl_ul").siblings().find("a").removeClass("on");
            };
            var index = 1;
            $(".wrap_MFl_ul_top").on("click",function () {
                $(this).parent().find(".ulList").slideToggle("fast",function () {
                    index *= -1;
                    if (index == 1){
                        $(this).parent().parent().find(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_xl_pre.png");
                    }else{
                        $(this).parent().parent().find(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_shou.png");
                    }
                })
            });
            $(".ulListOn").on("click",function () {
                $(this).addClass("on").parents(".wrap_MFl_ul").siblings().find("a").removeClass("on");
                $(this).addClass("on").parent().siblings().find("a").removeClass("on");
            });
        }
    }).state("basicData.partList1",{
        url:"/partList1",
        templateUrl:"../BasicData/partManage/partList.html",
        controller:"controllerPartList1"
    }).state("basicData.basicDataBuy",{
        url:"/basicDataBuy",
        templateUrl:"../BasicData/basicDataBuy/basicDataBuy.html",
        controller:"controllerBasicDataBuy"
    }).state("basicData.basicDataStock",{
        url:"/basicDataStock",
        templateUrl:"../BasicData/basicDataStock/basicDataStock.html",
        controller:"controllerBasicDataStock"
    }).state("callCenter",{
        url:"/callCenter",
        templateUrl:"../CallCenter/callCenter.html",
        controller:function () {
            var wrap_MFl_p = document.getElementById("wrap_MFl_p");
            wrap_MFl_p.innerHTML = sessionStorage.userName1;
            if (paths[1] == "#" ){
                $("#one").addClass("on")
            }else{
                $(paths[1]).addClass("on").parents(".wrap_MFl_ul").siblings().find("a").removeClass("on");
                $(paths[1]).addClass("on").parent().siblings().find("a").removeClass("on");
            };
            var index = 1;
            $(".wrap_MFl_ul_top").on("click",function () {
                $(this).parent().find(".ulList").slideToggle("fast",function () {
                    index *= -1;
                    if (index == 1){
                        $(this).parent().parent().find(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_xl_pre.png");
                    }else{
                        $(this).parent().parent().find(".wrap_MFl_ul_top_img2").attr("src","../Quote/img/home/leftbar_shou.png");
                    }
                })
            });
            $(".ulListOn").on("click",function () {
                $(this).addClass("on").parents(".wrap_MFl_ul").siblings().find("a").removeClass("on");
                $(this).addClass("on").parent().siblings().find("a").removeClass("on");
            });
        }
    }).state("callCenter.allCenter",{
        url:"/allCenter",
        templateUrl:"../CallCenter/allCenter/allCenter.html",
        controller:"controllerAllCenter"
    }).state("callCenter.waitOrder",{
        url:"/waitOrder",
        templateUrl:"../CallCenter/waitOrder/waitOrder.html",
        controller:"controllerWaitOrder"
    }).state("callCenter.overTimeOrder",{
        url:"/overTimeOrder",
        templateUrl:"../CallCenter/overTimeOrder/overTimeOrder.html",
        controller:"controllerOverTimeOrder"
    }).state("callCenter.teacherReassignment",{
        url:"/teacherReassignment",
        templateUrl:"../CallCenter/teacherReassignment/teacherReassignment.html",
        controller:"controllerTeacherReassignment"
    }).state("callCenter.serviceOrder",{
        url:"/serviceOrder",
        templateUrl:"../CallCenter/serviceOrder/serviceOrder.html",
        controller:"controllerServiceOrder"
    }).state("callCenter.historyOrder",{
        url:"/historyOrder",
        templateUrl:"../CallCenter/historyOrder/historyOrder.html",
        controller:"controllerHistoryOrderOne"
    }).state("callCenter.returnVisit",{
        url:"/returnVisit",
        templateUrl:"../CallCenter/returnVisit/returnVisit.html",
        controller:"controllerReturnVisit"
    }).state("callCenter.returnVisit1",{
        url:"/returnVisit1",
        templateUrl:"../CallCenter/returnVisit1/returnVisit.html",
        controller:"controllerReturnVisit1"
    }).state("userManage.blackList",{
        url:"/blackList",
        templateUrl:"../UserManagement/blackList/blackList.html",
        controller:"controllerBlackList"
    }).state("detailMaster",{
        url:"/detailMaster/:id",
        templateUrl:"../deTail/detailMaster/detailMaster.html",
        controller:"controllerDetailMaster"
    }).state("detailUser",{
        url:"/detailUser/:id",
        templateUrl:"../deTail/detailUser/detailUser.html",
        controller:"controllerDetailUser"
    }).state("detailCompany",{
        url:"/detailCompany/:id",
        templateUrl:"../deTail/detailCompany/detailCompany.html",
        controller:"controllerBlackList"
    })
});

