var app = angular.module("systemManage.systemSet",[]);
app.controller("controllerSystemSet",["$scope","$http","utilService",function ($scope,$http,utilService) {
    $scope.baseURL = utilService.baseURL();
    $(".sapn400reset").on("click",function () {
        $scope.companyName = '';
        $scope.companyAddress = '';
        $scope.companyEmail ='';
        $scope.serviceTel = '';
        $scope.complaintTel='';
        $scope.version_info = '';
        $scope.records_info = '';
    });
    $http.get($scope.baseURL+"bg/getSysInfo?token="+localStorage.token).success(function (res) {
        var item = res.data;
        console.log(item);
        $scope.companyName = item.company_name;
        $scope.companyAddress = item.address;
        $scope.companyEmail = item.email;
        $scope.serviceTel = item.service_tel;
        $scope.complaintTel=item.complaint_tel;
        $scope.version_info = item.version_info;
        $scope.records_info = item.records_info;

    })
    $(".sapn400queren").on("click",function () {
        $http.get($scope.baseURL+"bg/updateSysInfo?token="+localStorage.token+"&name="+$scope.companyName +"&address="+$scope.companyAddress+"&email="+$scope.companyEmail
            +"&serviceTel="+$scope.serviceTel+"&complaintTel="+$scope.complaintTel+"&recordsInfo="+$scope.records_info+"&versionInfo="+$scope.version_info).success(function (res) {
            if (res.state == 0){
                alert("更新成功！！！")
            }else {
                alert("更新失败  请检查！！！")
            }
        })
    });
    $(".span400_1_1").on("click",function () {
        $(this).addClass("on").siblings().removeClass("on");
    });
}]);