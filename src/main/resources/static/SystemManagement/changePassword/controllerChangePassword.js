var app  = angular.module("systemManage.passWord1",[]);
app.controller("controllerChangePassword",["$scope","$http","utilService","$window",function ($scope,$http,$window,utilService) {
    $scope.baseURL = utilService.baseURL();
    $scope.staffName = "李某某";
    $scope.flag = false;
    $scope.passWrodValue = "";
    $scope.send = function () {
        $scope.oldPassword = $(".oldPassword").val();
        $scope.newPassword = $(".newPassword").val();
        $scope.newPasswordAgin = $(".newPasswordAgin").val();
        $scope.userNo = $(".userNo").text();
        var password =  $scope.newPassword;
        if ($scope.newPassword == $scope.newPasswordAgin ){
            CheckPassWord(password)
        }else {
            $scope.flag = true;
        }
    };
    $scope.reset = function () {
        $(".oldPassword").val('');
        $(".newPassword").val('');
        $(".newPasswordAgin").val('');
        Dfault_color="#eeeeee";
        Lcolor=Mcolor=Hcolor=Dfault_color;
        document.getElementById("passWordStrength_L").style.background=Lcolor;
        document.getElementById("passWordStrength_M").style.background=Mcolor;
        document.getElementById("passWordStrength_R").style.background=Hcolor;
        return;
    }
    function CheckPassWord(password) {//必须为字母加数字且长度不小于8位
        var str = password;
        if (str == null || str.length <6) {
            alert("密码过于简单，最少六位密码")
            return false;
        }
        var reg1 = new RegExp(/^[0-9A-Za-z]+$/);
        if (!reg1.test(str)) {
            alert("密码不能存在特殊符号")
            return false;
        }
        var reg = new RegExp(/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/);
        if (reg.test(str)) {
            $.post($scope.baseURL+"bg/updatePwd",{
                token:localStorage.token,
                userNo:sessionStorage.userNo,
                oldPwd:$scope.oldPassword,
                newPwd:$scope.newPassword
            },function (res) {
                $("#system").css("display","none")
                $("#successM").css("display","block")
            })
        } else {
            return false;
        }
    }
    //返回强度级别
    function checkStrong(password){
        if (password.length<6){
            return 0;
        }else if (password.length<10 && password.length>=6){
            return 1
        }else if (password.length<14 && password.length>=10){
            return 2
        }else if (password.length>=14){
            return 3
        }
    }
    //显示颜色
    $scope.pwStrength = function (password){
        Dfault_color="#eeeeee";     //默认颜色
        L_color="#FF0000";      //低强度的颜色，且只显示在最左边的单元格中
        M_color="#FF9900";      //中等强度的颜色，且只显示在左边两个单元格中
        H_color="#33CC00";      //高强度的颜色，三个单元格都显示
        if (password==null||password==''){
            Lcolor=Mcolor=Hcolor=Dfault_color;
        }
        else{
            S_level=checkStrong(password);
            switch(S_level) {
                case 0:
                    Lcolor=Mcolor=Hcolor=Dfault_color;
                    break;
                case 1:
                    Lcolor=L_color;
                    Mcolor=Hcolor=Dfault_color;
                    break;
                case 2:
                    Lcolor=Mcolor=M_color;
                    Hcolor=Dfault_color;
                    break;
                default:
                    Lcolor=Mcolor=Hcolor=H_color;
            }
        }
        document.getElementById("passWordStrength_L").style.background=Lcolor;
        document.getElementById("passWordStrength_M").style.background=Mcolor;
        document.getElementById("passWordStrength_R").style.background=Hcolor;
        return;
    }
    //修改密码完成重新登录
    $scope.goLogin = function () {
        $window.location.href = "../../logIn.html"
    }
}]);