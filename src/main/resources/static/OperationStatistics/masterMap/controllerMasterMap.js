var app = angular.module("operation.masterMap",[]);
app.controller("controllerMasterMap",["$scope","$http","$stateParams","utilService",function ($scope,$http,$stateParams,utilService) {
    $scope.baseURL = utilService.baseURL();
// 获取上个界面传递的数据，并进行解析 作为订单位置
    if (sessionStorage.jsonString != undefined) {
        $scope.pointOrder = angular.fromJson(sessionStorage.jsonString);
        console.log($scope.pointOrder);
        $scope.lati= Number($scope.pointOrder.lati);
        $scope.lon= Number($scope.pointOrder.lon);
        $scope.orderNo= Number($scope.pointOrder.orderNo);
    }else {
        $scope.lati= 30.307036;
        $scope.lon= 120.107185;
    }
    $scope.goMap = function () {
        sessionStorage.setItem("infoWindowBtn",false)
        $state.go("First.five")
    };

    // 百度地图API功能
    $http.get($scope.baseURL+"bg/findAllOnline?token="+localStorage.token).success(function (res) {
        $scope.item = res.data;
        var pointArray = $scope.item;
        map = new BMap.Map("allmap");
        map.centerAndZoom(new BMap.Point($scope.lon,$scope.lati), 15);
        var markerOrder = new BMap.Marker(new BMap.Point($scope.lon,$scope.lati));
        var labelOrder = new BMap.Label("我是订单",{offset:new BMap.Size(20,-10)});
        markerOrder.setLabel(labelOrder);
        map.addOverlay(markerOrder);
        map.enableScrollWheelZoom();
        var opts = {
            width : 250,     // 信息窗口宽度
            height: 110,     // 信息窗口高度
            title : "工程师详情" , // 信息窗口标题
            enableMessage:true//设置允许信息窗发送短息
        };
        for(var i=0;i<pointArray.length;i++){
            var label = new BMap.Label("工程师姓名："+pointArray[i].name,{offset:new BMap.Size(20,-10)});
            var marker = new BMap.Marker(new BMap.Point(pointArray[i].lon,pointArray[i].lati));  // 创建标注
            marker.setLabel(label);
            map.addOverlay(marker);               // 将标注添加到地图中
            setInfoWindwo(pointArray[i],marker)
        };
        function setInfoWindwo(appUser,marker) {
            var content = "<div class='pointAddress' style='overflow: hidden;margin-top: -20px'>" +
                "<div class='divLeft' style='float: left;border-radius: 5px;margin-top: 24px;'>" +"<p>"+"<span>"+appUser.name+":"
                +"</span>"+"<span style='display: none' id='job_no'>"+appUser.job_no+
                "</span>"+"<span style='margin-left: 10px;'>"+(appUser.tel == undefined ? "暂无" : appUser.tel)+"</span>"+"</p>"+"<p>"+"<span>"+"位置："+"</span><span style='color: green'>"+(appUser.loc == undefined ? "暂无":appUser.loc)+
                "</span>"+"</p>"+"<p>"+"<span>"+"单数:"+"</span>"+"<span style='margin-left: 10px;color: green'>"+appUser.order_count+"单"+
                "</span>"+"</p>"+"<p>"+"<span>"+"距离:"+"</span>"+"<span style='margin-left: 10px;color: green'>"+distance(new BMap.Point($scope.lon,$scope.lati),new BMap.Point(appUser.lon,appUser.lati))+
                "</span>"+"</p>"
                +"</div>"+"<div id='divRight' style='float: right;height: 130px;width:50px;background-color: green;color: white;text-align: center;line-height: 90px;position: relative;top: 20px;'>派\n单</div>"+
                "</div>";
            addClickHandler(content,marker);
        }

        $scope.$on("updateLoc",function (event, data) {
            var item = data.data;
            for(var i = 0;i < pointArray.length;i++){
                if (pointArray[i].job_no == item.job_no){
                    pointArray[i].lon = item.lon;
                    pointArray[i].lati = item.lati;
                    update(pointArray[i]);
                    break
                }
            }

        });

        function update(appUser) {
            var allOverlay = map.getOverlays();
            for (var i = 0;i < allOverlay.length;i++ ){
                console.log(allOverlay[i].getLabel().content);
                if (allOverlay[i].getLabel().content.indexOf(appUser.name) >= 0){
                    map.removeOverlay(allOverlay[i]);
                    var marker = new BMap.Marker(new BMap.Point(appUser.lon,appUser.lati));
                    var label = new BMap.Label("工程师姓名："+appUser.name,{offset:new BMap.Size(20,-10)});
                    marker.setLabel(label);
                    map.addOverlay(marker);
                    setInfoWindwo(appUser,marker);
                    break;
                }
            }
        }
        function addClickHandler(content,marker){
            marker.addEventListener("click",function(e){
                openInfo(content,e)
            });
        }
        function distance(a,b) {
            (map.getDistance(a,b)).toFixed(2)
            var distance = (map.getDistance(a,b));
            if (distance < 1000){
                return distance.toFixed(0) + "米";
            }else {
                return (distance/1000).toFixed(2) + "千米"
            }
        }
        function openInfo(content,e){
            var p = e.target;
            var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
            var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象
            map.openInfoWindow(infoWindow,point); //开启信息窗口

            setTimeout(function () {
                document.getElementById("divRight").onclick = function () {
                    var orderNo = $("#job_no").text();
                    alert($scope.orderNo);
                    alert(orderNo);
                    alert(localStorage.token);
                    $http.get($scope.baseURL+"bg/distributeOrder?token="+localStorage.token+"&orderNo="+$scope.orderNo+"&jobNo="+orderNo).success(function (res) {
                        if (res.state == 0){
                            alert(res.message);
                        }else {
                            alert(res.message);
                        }
                    })
                };
                if (sessionStorage.infoWindowBtn == "true"){
                    console.log(typeof sessionStorage.infoWindowBtn);
                    document.getElementById("divRight").style.display = "block";
                }else {
                    console.log(typeof sessionStorage.infoWindowBtn);
                    document.getElementById("divRight").style.display = "none";
                }
            },1);
        }
    });
}]);