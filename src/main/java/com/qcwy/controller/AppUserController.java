package com.qcwy.controller;

import com.github.pagehelper.PageHelper;
import com.qcwy.entity.*;
import com.qcwy.entity.bg.BgOrder;
import com.qcwy.service.*;
import com.qcwy.utils.DateUtils;
import com.qcwy.utils.JedisUtil;
import com.qcwy.utils.JsonResult;
import com.qcwy.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by KouKi on 2017/2/13.
 */

@RestController
@RequestMapping("/app")
@Api("工程师端用户")
public class AppUserController {
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private WarehouseEmployeeService warehouseEmployeeService;
    @Autowired
    private PartService partService;
    @Autowired
    private WxUserService wxUserService;

    @PostMapping("/login")
    @ApiOperation("登录")
    public JsonResult<?> login(@ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                               @ApiParam(required = true, name = "pwd", value = "密码") @RequestParam(value = "pwd") String pwd) {
        if (StringUtils.isEmpty(jobNo) || StringUtils.isEmpty(pwd)) {
            return new JsonResult<>("用户名或密码不能为空");
        }
        AppUser appUser = appUserService.login(jobNo, pwd);
        if (appUser != null) {
            return new JsonResult<>(appUser.getId());
        } else {
            return new JsonResult<>("用户名或密码错误!");
        }
    }

    @GetMapping("/findAllOnline")
    @ApiOperation("查询所有在线工程师")
    public JsonResult<?> findAllOnline() {
        List<AppUser> appUsers;
        try {
            appUsers = appUserService.findAllOnline();
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(appUsers);
    }

    @GetMapping("/updateLoc")
    @ApiOperation("工程师实时上传位置")
    public JsonResult<?> updateLoc(@ApiParam(required = true, name = "lati", value = "维度") @RequestParam(value = "lati") String lati,
                                   @ApiParam(required = true, name = "lon", value = "经度") @RequestParam(value = "lon") String lon,
                                   @ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo) {
        try {
            Timestamp updateTime = new Timestamp(System.currentTimeMillis());
            appUserService.updateLoc(lati, lon, updateTime, jobNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    @PostMapping("/updatePwd")
    @ApiOperation("修改密码")
    public JsonResult<?> updatePwd(@ApiParam(required = true, name = "pwd", value = "新密码") @RequestParam(value = "pwd") String pwd,
                                   @ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo) {
        try {
            appUserService.updatePwd(pwd, jobNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    @PostMapping("/updateName")
    @ApiOperation("修改名字")
    public JsonResult<?> updateName(@ApiParam(required = true, name = "name", value = "姓名") @RequestParam(value = "name") String name,
                                    @ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo) {
        try {
            appUserService.updateName(name, jobNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //抢单
    @GetMapping("/rushOrder")
    @ApiOperation("抢单")
    public JsonResult<?> rushOrder(@ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                                   @ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        try {
            orderService.rushOrder(jobNo, orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //查询所有未抢订单
    @GetMapping("/getRushOrders")
    @ApiOperation("根据工程师位置查询所有未抢订单")
    public JsonResult<?> getRushOrders(@ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                                       @ApiParam(required = true, name = "lati", value = "维度") @RequestParam(value = "lati") String lati,
                                       @ApiParam(required = true, name = "lon", value = "经度") @RequestParam(value = "lon") String lon) {
        List<Order> orders;
        try {
            orders = orderService.findFreeOrders(jobNo, lati, lon);
            if (orders.isEmpty()) {
                return new JsonResult<>("当前没有可抢订单");
            }
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(orders);
    }

    //获取当前订单
    @GetMapping("/getCurrentOrders")
    @ApiOperation("获取工程师当前订单")
    public JsonResult<?> getCurrentOrders(@ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo) {
        List<Order> orders;
        try {
            orders = orderService.getHoldOrders(jobNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(orders);
    }

    //开始订单
    @GetMapping("/startOrder")
    @ApiOperation("开始订单")
    public JsonResult<?> startOrder(@ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                                    @ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        try {
            orderService.startOrder(jobNo, orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //暂停订单
    @GetMapping("/pauseOrder")
    @ApiOperation("暂停订单")
    public JsonResult<?> stopOrder(@ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                                   @ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        try {
            orderService.stopOrder(jobNo, orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //通过工号查询姓名
    @GetMapping("/getUsername")
    @ApiOperation("通过工号查询姓名")
    public JsonResult<?> getUsername(@ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo) {
        String username;
        try {
            username = appUserService.getUserNmae(jobNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(0, username);
    }

    //改派给指定工程师
    @GetMapping("/reassignmentToEngineer")
    @ApiOperation("改派给指定工程师")
    public JsonResult<?> reassignmentToEngineer(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo,
                                                @ApiParam(required = true, name = "oldNo", value = "改派人工号") @RequestParam(value = "oldNo") String oldNo,
                                                @ApiParam(required = true, name = "newNo", value = "接手人工号") @RequestParam(value = "newNo") String newNo,
                                                @ApiParam(required = true, name = "cause", value = "原因") @RequestParam(value = "cause") String cause) {
        if (StringUtils.isEmpty(oldNo)) {
            return new JsonResult<>("改派人工号为空");
        }
        if (!oldNo.equals(orderService.getJobNoByOrderNo(orderNo))) {
            return new JsonResult<>("订单号与工号不匹配");
        }
        OrderReassignment reassignment = new OrderReassignment();
        reassignment.setOrder_no(orderNo);
        reassignment.setSend_time(DateUtils.format(new Date(), "yyyyMMddHHmmss"));
        reassignment.setOld_no(oldNo);
        reassignment.setNew_no(newNo);
        reassignment.setCause(cause);
        try {
            orderService.reassignmentOrder(reassignment);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //获取改派工程师姓名
    @GetMapping("/getReassignmentName")
    @ApiOperation("获取改派工程师姓名")
    public JsonResult<?> getReassignmentName(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        String name;
        try {
            name = appUserService.getReassignmentName(orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(0, name);
    }

    //改派给后台
    @GetMapping("/reassignmentToBackground")
    @ApiOperation("改派给后台")
    public JsonResult<?> reassignmentToBackground(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo,
                                                  @ApiParam(required = true, name = "jobNo", value = "改派人工号") @RequestParam(value = "jobNo") String jobNo,
                                                  @ApiParam(required = true, name = "cause", value = "原因") @RequestParam(value = "cause") String cause) {
        if (StringUtils.isEmpty(jobNo)) {
            return new JsonResult<>("改派人工号为空");
        }
        if (!jobNo.equals(orderService.getJobNoByOrderNo(orderNo))) {
            return new JsonResult<>("订单号与工号不匹配");
        }
        BgOrder bgOrder = new BgOrder();
        bgOrder.setOrder_no(orderNo);
        bgOrder.setType(1);//1-改派
        bgOrder.setState(0);//0-未处理
        bgOrder.setCause(StringUtils.isEmpty(cause) ? "" : cause);
        bgOrder.setHandle_time(new Timestamp(0));
        try {
            orderService.saveBgOrder(bgOrder);
            orderService.updateOrderState(orderNo, 6);//6-改派中
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //查询工程师订单消息
    @GetMapping("/getOrderMessage")
    @ApiOperation("查询工程师订单消息")
    public JsonResult<?> getOrderMessage(@ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo) {
        List<AppOrderMessage> messages;
        try {
            messages = appUserService.getOrderMsgByJobNo(jobNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(messages);
    }

    //根据单号获取订单
    @GetMapping("/getOrderByOrderNo")
    @ApiOperation("根据单号获取订单")
    public JsonResult<?> getOrderByOrderNo(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        Order order;
        try {
            order = orderService.getOrderByOrderNo(orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        if (order == null) {
            return new JsonResult<>("订单号不存在");
        } else {
            return new JsonResult<>(order);
        }
    }

    //获取改派详情
    @GetMapping("/getReassignment")
    @ApiOperation("获取改派详情")
    public JsonResult<?> getReassignment(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        OrderReassignment orderReassignment;
        try {
            orderReassignment = appUserService.getReassignment(orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        if (orderReassignment == null) {
            return new JsonResult<>("未查到改派订单");
        } else {
            return new JsonResult<>(orderReassignment);
        }
    }

    //改预约单
    @GetMapping("/appointmentOrder")
    @ApiOperation("改预约单")
    public JsonResult<?> appointmentOrder(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo,
                                          @ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                                          @ApiParam(required = true, name = "cause", value = "原因") @RequestParam(value = "cause") String cause,
                                          @ApiParam(required = true, name = "time", value = "预约时间") @RequestParam(value = "time") long time,
                                          @ApiParam(required = true, name = "lon", value = "经度") @RequestParam(value = "lon") double lon,
                                          @ApiParam(required = true, name = "lati", value = "维度") @RequestParam(value = "lati") double lati,
                                          @ApiParam(required = true, name = "loc", value = "位置描述") @RequestParam(value = "loc") String loc) {
        if (StringUtils.isEmpty(jobNo)) {
            return new JsonResult<>("工号为空");
        }
        if (!jobNo.equals(orderService.getJobNoByOrderNo(orderNo))) {
            return new JsonResult<>("订单与工号不匹配");
        }
        OrderAppointment orderAppointment = new OrderAppointment();
        orderAppointment.setOrder_no(orderNo);
        orderAppointment.setJob_no(jobNo);
        orderAppointment.setCause(cause);
        orderAppointment.setTime(new Timestamp(time));
        orderAppointment.setLon(lon);
        orderAppointment.setLati(lati);
        orderAppointment.setLoc(loc);
        OrderDetail orderDetail = orderService.getOrderDetail(orderNo);
        try {
            orderService.updateInfo(orderAppointment);
            orderService.updateOrderState(orderNo, 4);//4-预约
            orderService.updateAppointmentTime(new Timestamp(time), orderNo);
            orderAppointment.setLon(Double.valueOf(orderDetail.getLon()));
            orderAppointment.setLati(Double.valueOf(orderDetail.getLati()));
            orderAppointment.setLoc(orderDetail.getLoc());
            orderService.saveOrderAppointment(orderAppointment);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //接受改派订单
    @GetMapping("/acceptOrder")
    @ApiOperation("接受改派订单")
    public JsonResult<?> acceptOrder(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo,
                                     @ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo) {
        Timestamp handleTime = new Timestamp(System.currentTimeMillis());
        try {
            appUserService.acceptReassignment(handleTime, orderNo, jobNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //拒绝改派订单
    @GetMapping("/refuseOrder")
    @ApiOperation("拒绝改派订单")
    public JsonResult<?> refuseOrder(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo,
                                     @ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo) {
        Timestamp handleTime = new Timestamp(System.currentTimeMillis());
        try {
            appUserService.refuseReassignment(handleTime, orderNo, jobNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //获取员工仓所有零件
    @GetMapping("/getParts")
    @ApiOperation("获取员工仓所有零件")
    public JsonResult<?> getParts(@ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo) {
        List<WarehouseEmployee> warehouseEmployees;
        try {
            warehouseEmployees = warehouseEmployeeService.getParts(jobNo);
            for (WarehouseEmployee warehouseEmployee : warehouseEmployees) {
                PartDetail partDetail = partService.getPartDetailByPartId(warehouseEmployee.getPart_detail_id());
                warehouseEmployee.setName(partDetail.getName());
                warehouseEmployee.setModle(partDetail.getModel());
            }
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(warehouseEmployees);
    }

    //确认故障
    @PostMapping("/confirmTrouble")
    @ApiOperation("确认故障")
    public JsonResult<?> confirmTrouble(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo,
                                        @ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                                        @ApiParam(required = true, name = "faultId", value = "故障Id") @RequestParam(value = "faultId") String faultId,
                                        @ApiParam(name = "faultDescription", value = "故障描述") @RequestParam(value = "faultDescription") String faultDescription,
                                        @ApiParam(name = "files", value = "图片") @RequestParam(value = "files") List<MultipartFile> files,
                                        @ApiParam(name = "parts", value = "零件") @RequestParam(value = "parts", required = false) List<String> parts) {
        if (parts == null) {
            parts = new ArrayList<>();
        }
        if (StringUtils.isEmpty(jobNo)) {
            return new JsonResult<>("工号不能为空");
        }
        //查询订单号对应的工号是否正确
        if (!jobNo.equals(orderService.getJobNoByOrderNo(orderNo))) {
            return new JsonResult<>("工号与订单号不匹配");
        }
        if (StringUtils.isEmpty(faultId)) {
            return new JsonResult<>("未选择故障");
        }
        try {
            orderService.confirmTroubleApp(orderNo, faultId, faultDescription, files, parts);
            WxUser wxUser = wxUserService.selectUserByOpenId(orderService.getOpenIdByOrderNo(orderNo));
            if (wxUser.getType() == 1) {//来自客服录入的用户
                orderService.updateOrderState(orderNo, 8);//修改订单状态为8--自动替用户确认故障
            }
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //维修完成
    @GetMapping("/complete")
    @ApiOperation("维修完成")
    public JsonResult<?> complete(@ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                                  @ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        if (StringUtils.isEmpty(jobNo)) {
            return new JsonResult<>("工号不能为空");
        }
        //查询订单号对应的工号是否正确
        if (!jobNo.equals(orderService.getJobNoByOrderNo(orderNo))) {
            return new JsonResult<>("工号与订单号不匹配");
        }
        try {
            orderService.complete(orderNo);
            WxUser wxUser = wxUserService.selectUserByOpenId(orderService.getOpenIdByOrderNo(orderNo));
            if (wxUser.getType() == 1) {//来自客服录入的用户
                orderService.updateOrderState(orderNo, 10);//修改订单状态为10--自动替用户验收
            }
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //获取历史订单数据
    @GetMapping("/getHistoryOrders")
    @ApiOperation("获取历史订单数据")
    public JsonResult<?> getHistoryOrders(@ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                                          @ApiParam(required = true, name = "startTime", value = "开始时间(yyyyMMddHHmmss)") @RequestParam(value = "startTime") String startTime,
                                          @ApiParam(required = true, name = "endTime", value = "结束时间(yyyyMMddHHmmss)") @RequestParam(value = "endTime") String endTime) {
        if (StringUtils.isEmpty(jobNo)) {
            return new JsonResult<>("工号为空");
        }
        List<Order> historyOrders;
        HistoryOrderData historyOrderData = new HistoryOrderData();
        int totalPrice = 0;
        int rushCount = 0;
        double percentRush;
        double percentLose;
        try {
            historyOrders = orderService.getHistoryOrder(jobNo, startTime, endTime);
            if (historyOrders.size() == 0) {
                return new JsonResult<>("未查询到订单");
            }
            //根据单号计算零件总额
            for (Order order : historyOrders) {
                if (order.getState() == 11) {
                    rushCount += 1;
                }
                OrderDetail orderDetail = orderService.getOrderDetail(order.getOrder_no());
                totalPrice += orderDetail.getTotal_price();
            }
            percentRush = (double) rushCount / historyOrders.size();
            percentRush = (double) ((int) (percentRush * 1000)) / 1000;
            percentLose = (double) (1000 - (int) (percentRush * 1000)) / 1000;
            historyOrderData.setCount(historyOrders.size());
            historyOrderData.setTotalPrice(totalPrice);
            historyOrderData.setRushCount(rushCount);
            historyOrderData.setLoseCount(historyOrders.size() - rushCount);
            historyOrderData.setPercentRush(percentRush);
            historyOrderData.setPercentLose(percentLose);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(historyOrderData);
    }

    //获取历史订单数据列表
    @GetMapping("/getHistoryOrderList")
    @ApiOperation("获取历史订单列表")
    public JsonResult<?> getHistoryList(@ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo,
                                        @ApiParam(required = true, name = "startTime", value = "开始时间(yyyyMMddHHmmss)") @RequestParam(value = "startTime") String startTime,
                                        @ApiParam(required = true, name = "endTime", value = "结束时间(yyyyMMddHHmmss)") @RequestParam(value = "endTime") String endTime,
                                        @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                        @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders;
        try {
            orders = orderService.getHistoryOrder(jobNo, startTime, endTime);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(orders);
    }

    //获取工程师确认故障
    @GetMapping("/getRealOrderFault")
    @ApiOperation("获取工程师确认故障")
    public JsonResult<?> getOrderFault(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        OrderFault orderFault;
        try {
            orderFault = orderService.getOrderFault(orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(orderFault);
    }

    //获取订单零件
    @GetMapping("/getOrderPart")
    @ApiOperation("获取订单零件")
    public JsonResult<?> getOrderPart(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        List<OrderPart> orderParts;
        try {
            orderParts = orderService.getOrderParts(orderNo);
            for (OrderPart orderPart : orderParts) {
                PartDetail partDetail = partService.getPartDetailByPartId(orderPart.getPart_detail_id());
                orderPart.setPartMode(partDetail.getModel());
                orderPart.setPrice(partDetail.getPrice());
            }
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(orderParts);
    }

    //获取预约单信息
    @GetMapping("/getAppointment")
    @ApiOperation("获取预约单信息")
    public JsonResult<?> getAppointment(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        OrderAppointment orderAppointment;
        try {
            orderAppointment = orderService.getAppointment(orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(orderAppointment);
    }

    //获取工程师诊断
    @GetMapping("/getRealTrouble")
    @ApiOperation("获取工程师诊断")
    public JsonResult<?> getRealTrouble(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        OrderFault orderFault;
        try {
            orderFault = orderService.getOrderFault(orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(orderFault);
    }

    //确定线下支付
    @GetMapping("/offlinePay")
    @ApiOperation("确定用户线下付款")
    public JsonResult<?> offlinePay(@ApiParam(required = true, name = "jobNo", value = "jobNo") @RequestParam(value = "jobNo") String jobNo,
                                    @ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        try {
            if (!jobNo.equals(orderService.getJobNoByOrderNo(orderNo))) {
                return new JsonResult<>("订单数据异常");
            }
            if (orderService.getOrder(orderNo).getState() == 11) {
                return new JsonResult<>("该订单已付款");
            }
            if (orderService.getOrder(orderNo).getState() != 10) {
                return new JsonResult<>("");
            }
            orderService.offlinePay(orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //获取订单数量排名
    @GetMapping("/getOrderCountRank")
    @ApiOperation("获取订单数量排名")
    public JsonResult<?> getOrderCountRank(@ApiParam(required = true, name = "date", value = "日期(yyyy/yyyyMM/yyyyMMdd)") @RequestParam(value = "date") String date,
                                           @ApiParam(required = true, name = "pageNum", value = "pageNum") @RequestParam(value = "pageNum") int pageNum,
                                           @ApiParam(required = true, name = "pageSize", value = "pageSize") @RequestParam(value = "pageSize") int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Rank> ranks;
        try {
            ranks = orderService.getOrderCountRankByDate(date);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(ranks);
    }

    //获取当月订单数量
    @GetMapping("/getOrderCount")
    @ApiOperation("获取当月订单数量")
    public JsonResult<?> getOrderCount(@ApiParam(required = true, name = "jobNo", value = "工号") @RequestParam(value = "jobNo") String jobNo) {
        int count;
        try {
            count = appUserService.getOrderCount(jobNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(count);
    }

    //微信认证
    // 自定义 token
//    private String TOKEN = "weixin";
//    @GetMapping("/token")
//    public void token(HttpServletRequest request, HttpServletResponse response){
//        // 微信加密签名
//        String signature = request.getParameter("signature");
//        // 随机字符串
//        String echostr = request.getParameter("echostr");
//        // 时间戳
//        String timestamp = request.getParameter("timestamp");
//        // 随机数
//        String nonce = request.getParameter("nonce");
//
//        String[] str = { TOKEN, timestamp, nonce };
//        Arrays.sort(str); // 字典序排序
//        String bigStr = str[0] + str[1] + str[2];
//        try {
//            response.getWriter().print(echostr);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}