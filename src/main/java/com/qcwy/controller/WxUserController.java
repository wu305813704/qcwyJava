package com.qcwy.controller;

import com.github.pagehelper.PageHelper;
import com.qcwy.entity.*;
import com.qcwy.service.AppUserService;
import com.qcwy.service.OrderService;
import com.qcwy.service.WxUserService;
import com.qcwy.utils.DateUtils;
import com.qcwy.utils.JsonResult;
import com.qcwy.utils.StringUtils;
import com.qcwy.utils.wx.UnifiedOrderNotifyRequestData;
import com.qcwy.utils.wx.WeixinConstant;
import com.qcwy.utils.wx.WeixinPayConfig;
import com.qcwy.utils.wx.WxUtils;
import com.qcwy.websocket.WxWebSocket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KouKi on 2017/2/13.
 */

@RestController
@RequestMapping("/wx")
@Api("微信端用户")
public class WxUserController {
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private OrderService orderService;

    //通过code授权登录
    @GetMapping("/accredit")
    @ApiOperation("授权登录")
    public JsonResult<?> accredit(@ApiParam(required = true, name = "code", value = "code") @RequestParam(value = "code") String code) {
        try {
            WxAccessToken token = wxUserService.getAccessToken(code);
            if (token.getOpenid() == null) {
                return new JsonResult<>("无效的code");
            }
            //通过openId查询用户是否已注册
            WxUser user = wxUserService.selectUserByOpenId(token.getOpenid());
            //联网获取用户信息
            WxUser wxUser = WxUtils.getUserInfo(token.getAccess_token(), token.getOpenid());
            if (user == null) {
                //保存进数据库
                wxUserService.register(wxUser);
                return new JsonResult<>(wxUser);
            } else {
                //联网获取用户信息
                //更新数据库
                wxUserService.updateWxUser(wxUser);
                return new JsonResult<>(wxUser);
            }
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
    }

    //通过openId查询是否注册
    @GetMapping("/getUser")
    @ApiOperation("通过openId获取用户详情")
    public JsonResult<?> getUser(@ApiParam(required = true, name = "openId", value = "微信唯一标识ID") @RequestParam(value = "openId") String openId) {
        WxUser wxUser = wxUserService.selectUserByOpenId(openId);
        if (wxUser != null) {
            return new JsonResult<>(wxUser);
        } else {
            return new JsonResult<>("未注册");
        }
    }

//    //注册
//    @PostMapping("/register")
//    @ApiOperation("注册")
//    public JsonResult<?> register(@ApiParam(required = true, name = "openId", value = "openId") @RequestParam(value = "openId") String openId,
//                                  @ApiParam(required = true, name = "nickname", value = "昵称") @RequestParam(value = "nickname") String nickname,
//                                  @ApiParam(required = true, name = "sex", value = "性别") @RequestParam(value = "sex") String sex,
//                                  @ApiParam(required = true, name = "province", value = "省份") @RequestParam(value = "province") String province,
//                                  @ApiParam(required = true, name = "city", value = "城市") @RequestParam(value = "city") String city,
//                                  @ApiParam(required = true, name = "country", value = "国家") @RequestParam(value = "country") String country,
//                                  @ApiParam(required = true, name = "headimagurl", value = "头像地址") @RequestParam(value = "headimagurl") String headimagurl,
//                                  @ApiParam(required = true, name = "privilege", value = "特权") @RequestParam(value = "privilege") String privilege,
//                                  @ApiParam(required = true, name = "unionid", value = "unionid") @RequestParam(value = "unionid") String unionid) {
//        WxUser wxUser = new WxUser();
//        wxUser.setOpenid(openId);
//        wxUser.setNickname(nickname);
//        wxUser.setSex(sex);
//        wxUser.setProvince(province);
//        wxUser.setCity(city);
//        wxUser.setCountry(country);
//        wxUser.setHeadimgurl(headimagurl);
//        wxUser.setProvince(privilege);
//        wxUser.setUnionid(unionid);
//        wxUser.setRegist_time(new Timestamp(System.currentTimeMillis()));
//        try {
//            wxUserService.register(wxUser);
//        } catch (Exception e) {
//            return new JsonResult<>(e);
//        }
//        return new JsonResult<>(wxUser.getId());
//    }

    //添加手机号
    @PostMapping("/addTel")
    @ApiOperation("添加电话")
    public JsonResult<?> addTel(@ApiParam(required = true, name = "tel", value = "电话") @RequestParam(value = "tel") String tel,
                                @ApiParam(required = true, name = "openId", value = "微信唯一标识ID") @RequestParam(value = "openId") String openId) {
        int line;
        try {
            line = wxUserService.addTel(tel, openId);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        if (line == 1) {
            return new JsonResult<>(line);
        }
        return new JsonResult<>("未注册");
    }

    //获取用户当前订单
    @GetMapping("/getCurrentOrder")
    @ApiOperation("获取用户当前订单")
    public JsonResult<?> getCurrentOrder(@ApiParam(required = true, name = "openId", value = "openId") @RequestParam(value = "openId") String openId) {
        List<Order> order;
        try {
            order = wxUserService.getCurrentOrder(openId);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(order);
    }

    //获取用户历史订单
    @GetMapping("/getHistoryOrders")
    @ApiOperation("获取用户历史订单")
    public JsonResult<?> getHistoryOrders(@ApiParam(required = true, name = "openId", value = "openId") @RequestParam(value = "openId") String openId,
                                          @ApiParam(required = true, name = "pageNum", value = "页码") @RequestParam(value = "pageNum") int pageNum,
                                          @ApiParam(required = true, name = "pageSize", value = "每页大小") @RequestParam(value = "pageSize") int pageSize) {
        PageHelper.startPage(pageNum, pageSize);//分页查询
        List<Order> order;
        try {
            order = wxUserService.getHistoryOrders(openId);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(order);
    }

    //下普通订单
    @PostMapping("/placeOrder")
    @ApiOperation("下普通订单")
    public JsonResult<?> placeOrder(@ApiParam(required = true, name = "openId", value = "openId") @RequestParam(value = "openId") String openId,
                                    @ApiParam(required = true, name = "faultId", value = "故障ID(1-2-3)") @RequestParam(value = "faultId") String faultId,
                                    @ApiParam(name = "faultDescription", value = "故障详情") @RequestParam(value = "faultDescription") String faultDescription,
                                    @ApiParam(required = true, name = "lon", value = "经度") @RequestParam(value = "lon") double lon,
                                    @ApiParam(required = true, name = "lati", value = "维度") @RequestParam(value = "lati") double lati,
                                    @ApiParam(required = true, name = "loc", value = "位置描述") @RequestParam(value = "loc") String loc,
                                    @ApiParam(required = true, name = "carType", value = "车辆类型(0-两轮车1-三轮车)") @RequestParam(value = "carType") int carType) {
        if (!wxUserService.getTel(openId)) {
            return new JsonResult<>("请在个人中心填写电话");
        }
        //正则表达式匹配故障ID
        String regEx = "(^[1-9]\\d?)(-[1-9]\\d?)*$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(faultId);
        boolean rs = matcher.matches();
        if (!rs) {
            return new JsonResult<>("故障ID不匹配");
        }
        //查询用户是否有未完成的订单
        if (wxUserService.hasUndoneOrder(openId)) {
            return new JsonResult<>("有未完成的订单");
        }
        Order order = new Order();
        try {
            order.setOpen_id(openId);
            order.setType(0);//0-普通订单 1-预约单 2-售后订单
            order.setSend_time(new Timestamp(System.currentTimeMillis()));
            order.setCar_type(carType);
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setFault_id(faultId);
            orderDetail.setFault_description(faultDescription);
            orderDetail.setLon(String.valueOf(lon));
            orderDetail.setLati(String.valueOf(lati));
            orderDetail.setLoc(loc);
            //当前时间
            String sendTime = DateUtils.format(new Date(), "yyyyMMddHHmmss");
            orderDetail.setSend_time(sendTime);
            order.setOrderDetail(orderDetail);
            WxUser wxUser = wxUserService.selectUserByOpenId(order.getOpen_id());
            order.setWxUser(wxUser);
            orderService.placeOrder(order);
            //更新最后报修时间
            wxUserService.updateLastRepairsTime(openId);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(order.getOrder_no());
    }

    //预约单
    @PostMapping("/appointmentOrder")
    @ApiOperation("下预约单")
    public JsonResult<?> appointmentOrder(@ApiParam(required = true, name = "openId", value = "openId") @RequestParam(value = "openId") String openId,
                                          @ApiParam(required = true, name = "faultId", value = "故障ID(1-2-3)") @RequestParam(value = "faultId") String faultId,
                                          @ApiParam(name = "faultDescription", value = "故障详情") @RequestParam(value = "faultDescription") String faultDescription,
                                          @ApiParam(required = true, name = "lon", value = "经度") @RequestParam(value = "lon") double lon,
                                          @ApiParam(required = true, name = "lati", value = "维度") @RequestParam(value = "lati") double lati,
                                          @ApiParam(required = true, name = "loc", value = "位置描述") @RequestParam(value = "loc") String loc,
                                          @ApiParam(required = true, name = "appointmentTime", value = "预约时间") @RequestParam(value = "appointmentTime") long appointmentTime,
                                          @ApiParam(required = true, name = "carType", value = "车辆类型(0-两轮车1-三轮车)") @RequestParam(value = "carType") int carType) {
        //正则表达式匹配故障ID
        String regEx = "(^[1-9]\\d?)(-[1-9]\\d?)*$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(faultId);
        boolean rs = matcher.matches();
        if (!rs) {
            return new JsonResult<>("故障ID不匹配");
        }
        //查询用户是否有未完成的订单
        if (wxUserService.hasUndoneOrder(openId)) {
            return new JsonResult<>("有未完成的订单");
        }
        Order order = new Order();
        try {
            order.setOpen_id(openId);
            order.setType(1);//预约单
            order.setSend_time(new Timestamp(System.currentTimeMillis()));
            order.setAppointment_time(new Timestamp(appointmentTime));
            order.setCar_type(carType);
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setFault_id(faultId);
            orderDetail.setFault_description(faultDescription);
            orderDetail.setLon(String.valueOf(lon));
            orderDetail.setLati(String.valueOf(lati));
            orderDetail.setLoc(loc);
            orderDetail.setSend_time(DateUtils.format(new Date(), "yyyyMMddHHmmss"));
            orderDetail.setAppointment_time(DateUtils.format(new Date(appointmentTime), "yyyyMMddHHmmss"));
            order.setOrderDetail(orderDetail);
            WxUser wxUser = wxUserService.selectUserByOpenId(order.getOpen_id());
            order.setWxUser(wxUser);
            orderService.appointmentOrder(order);
            //更新最后报修时间
            wxUserService.updateLastRepairsTime(openId);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(order.getOrder_no());
    }

    //加价
    @GetMapping("/addPrice")
    @ApiOperation("加价")
    public JsonResult<?> addPrice(@ApiParam(required = true, name = "addPrice", value = "加价") @RequestParam(value = "addPrice") int addPrice,
                                  @ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        try {
            orderService.addPrice(addPrice, orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //确认故障
    @GetMapping("/confirmTrouble")
    @ApiOperation("确认故障")
    public JsonResult<?> confirmTrouble(@ApiParam(required = true, name = "openId", value = "openId") @RequestParam(value = "openId") String openId,
                                        @ApiParam(required = true, name = "orderNo", value = "orderNo") @RequestParam(value = "orderNo") int orderNo) {
        if (StringUtils.isEmpty(openId)) {
            return new JsonResult<>("openId不能为空");
        }
        try {
            orderService.confirmTroubleWx(openId, orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //拒绝确认故障
    @GetMapping("/rejectTrouble")
    @ApiOperation("拒绝故障")
    public JsonResult<?> rejectTrouble(@ApiParam(required = true, name = "openId", value = "openId") @RequestParam(value = "openId") String openId,
                                       @ApiParam(required = true, name = "orderNo", value = "orderNo") @RequestParam(value = "orderNo") int orderNo) {
        try {
            orderService.rejectTrouble(openId, orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
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

    //验收
    @GetMapping("/checkAndAccept")
    @ApiOperation("验收")
    public JsonResult<?> checkAndAccept(@ApiParam(required = true, name = "openId", value = "openId") @RequestParam(value = "openId") String openId,
                                        @ApiParam(required = true, name = "orderNo", value = "orderNo") @RequestParam(value = "orderNo") int orderNo) {
        if (StringUtils.isEmpty(openId)) {
            return new JsonResult<>("openId不能为空");
        }
        if (!openId.equals(orderService.getOpenIdByOrderNo(orderNo))) {
            return new JsonResult<>("订单数据异常");
        }
        try {
            orderService.checkAndAccept(orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //订单评价
    @GetMapping("/orderEvaluate")
    @ApiOperation("订单评价")
    public JsonResult<?> orderEvaluate(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo,
                                       @ApiParam(required = true, name = "serviceAttitude", value = "服务态度") @RequestParam(value = "serviceAttitude") int serviceAttitude,
                                       @ApiParam(required = true, name = "visitSpeed", value = "上门速度") @RequestParam(value = "visitSpeed") int visitSpeed,
                                       @ApiParam(required = true, name = "technicalAbility", value = "技术能力") @RequestParam(value = "technicalAbility") int technicalAbility,
                                       @ApiParam(name = "remark", value = "备注") @RequestParam(value = "remark") String remark) {
        if (orderService.getOrderEvaluate(orderNo) != null) {
            return new JsonResult<>("此订单已评价,不要重复评价");
        }
        OrderEvaluate orderEvaluate = new OrderEvaluate();
        orderEvaluate.setOrder_no(orderNo);
        orderEvaluate.setService_attitude(serviceAttitude);
        orderEvaluate.setVisit_speed(visitSpeed);
        orderEvaluate.setTechnical_ability(technicalAbility);
        orderEvaluate.setRemark(remark);
        orderEvaluate.setTime(new Timestamp(System.currentTimeMillis()));
        try {
            orderService.orderEvaluate(orderEvaluate);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //取消订单
    @GetMapping("/cancelOrder")
    @ApiOperation("取消订单")
    public JsonResult<?> cancelOrder(@ApiParam(required = true, name = "openId", value = "openId") @RequestParam(value = "openId") String openId,
                                     @ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo,
                                     @ApiParam(required = true, name = "cause", value = "取消原因(0-2-3)") @RequestParam(value = "cause") String cause) {
        //正则表达式匹配故障ID
        String regEx = "(^[0-9]\\d?)(-[0-9]\\d?)*$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(cause);
        boolean rs = matcher.matches();
        if (!rs) {
            return new JsonResult<>("数据异常");
        }
        if (StringUtils.isEmpty(openId)) {
            return new JsonResult<>("openId不能为空");
        }
        if (!openId.equals(orderService.getOpenIdByOrderNo(orderNo))) {
            return new JsonResult<>("订单数据异常");
        }
        OrderCancel orderCancel = new OrderCancel();
        orderCancel.setOrder_no(orderNo);
        orderCancel.setCause(cause);
        try {
            orderService.updateOrderState(orderNo, 5);
            wxUserService.cancelOrder(orderCancel);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(true);
    }

    //获取订单详情
    @GetMapping("/getOrderDetail")
    @ApiOperation("获取订单详情")
    public JsonResult<?> getOrderDetail(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        Order order;
        try {
            order = orderService.getOrder(orderNo);
            WxWebSocket.sendInfo("123");
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(order);
    }

    //获取订单零件
    @GetMapping("/getOrderPart")
    @ApiOperation("获取订单零件")
    public JsonResult<?> getOrderPart(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        List<OrderPart> orderParts;
        try {
            orderParts = orderService.getOrderParts(orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(orderParts);
    }

    //获取订单评价
    @GetMapping("/getOrderEvaluate")
    @ApiOperation("获取订单评价")
    public JsonResult<?> getOrderEvaluate(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        OrderEvaluate orderEvaluate;
        try {
            orderEvaluate = orderService.getOrderEvaluate(orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(orderEvaluate);
    }

    //获取旧品收入
    @GetMapping("/getOldPart")
    @ApiOperation("获取旧品收入")
    public JsonResult<?> getOldPart(@ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        //旧品收入
        List<OrderPart> orderParts;
        try {
            orderParts = orderService.getOldOrderPart(orderNo);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return new JsonResult<>(orderParts);
    }

    //生成前端需要预付款的数据包(微信支付)
    @PostMapping("/getAdvancePackege")
    @ApiOperation("生成前端需要预付款的数据包")
    public JsonResult<?> getAdvancePackege(@ApiParam(required = true, name = "openid", value = "openid") @RequestParam(value = "openid") String openid,
                                           @ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo,
                                           @ApiParam(required = true, name = "ip", value = "ip") @RequestParam(value = "ip") String ip) {
        JsonResult<?> responseData;
        try {
            int totalFee /*= (int) (orderService.getOrderDetail(orderNo).getTotal_price() * 100)*/;
            //TODO 测试全部改成 1 分钱
            totalFee = 1;
            responseData = WxUtils.getAdvancePackege(WeixinPayConfig.APP_ID, WeixinPayConfig.BODY, String.valueOf(orderNo), totalFee, ip, WeixinPayConfig.JSAPI, openid);
        } catch (Exception e) {
            return new JsonResult<>(e);
        }
        return responseData;
    }

    //支付结果通知
    @PostMapping("/notifyUrl")
    @ApiOperation("支付结果通知")
    public String notifyUrl(HttpServletRequest request) {
        //1、获取回调数据
        String result;
        BufferedReader br = null;
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = WxUtils.setXML(WeixinConstant.FAIL, "invalid data block");
            return result;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //2、把接收到的数据转换成UnifiedOrderNotifyRequestData对象
        UnifiedOrderNotifyRequestData payResult = WxUtils.castXMLStringToUnifiedOrderNotifyRequestData(sb.toString());
        System.out.println(payResult.getDevice_info());
        result = WxUtils.getCallbackResponseData(payResult, orderService);
        return result;
    }

    //申请线下支付
    @GetMapping("/aplayOfflinePay")
    @ApiOperation("微信用户申请线下支付")
    public JsonResult<?> aplayOfflinePay(@ApiParam(required = true, name = "openid", value = "openid") @RequestParam(value = "openid") String openid,
                                         @ApiParam(required = true, name = "orderNo", value = "订单号") @RequestParam(value = "orderNo") int orderNo) {
        if (StringUtils.isEmpty(openid)) {
            return new JsonResult<>("openid不能为空");
        }
        if (!openid.equals(orderService.getOpenIdByOrderNo(orderNo))) {
            return new JsonResult<>("订单数据异常");
        }
        //TODO 推送给工程师，该用户申请线下支付

        return new JsonResult<>(true);
    }

}
