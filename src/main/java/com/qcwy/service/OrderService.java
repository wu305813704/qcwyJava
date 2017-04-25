package com.qcwy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qcwy.entity.*;
import com.qcwy.entity.bg.BgOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

/**
 * Created by KouKi on 2017/2/16.
 */

public interface OrderService {
    //根据单号获取openId
    String getOpenIdByOrderNo(int orderNo);

    //根据单号获取订单
    Order getOrderByOrderNo(int orderNo);

    //根据单号获取job_no
    String getJobNoByOrderNo(int orderNo);

    //查询所有未抢订单
    List<Order> findFreeOrders(String jobNo, String lati, String lon) throws Exception;

    //下普通订单
    void placeOrder(Order order);

    //预约单
    void appointmentOrder(Order order) throws IOException;

    //加价
    void addPrice(int addPrice, int orderNo);

    //查询订单状态
    Integer getStateByNo(int orderNo);

    //抢单
    void rushOrder(String jobNo, int orderNo) throws Exception;

    //查询工程师所持有的订单
    List<Order> getHoldOrders(String jobNo);

    //修改订单状态
    void updateOrderState(int orderNo, int state);

    //开始订单
    void startOrder(String jobNo, int orderNo) throws Exception;

    //暂停订单
    void stopOrder(String jobNo, int orderNo) throws Exception;

    //改派
    void reassignmentOrder(OrderReassignment reassignment) throws IOException;

    //工程师确认故障
    void confirmTroubleApp(int orderNo, String faultId, String faultDescription,
                           List<MultipartFile> files, List<String> parts) throws NoSuchAlgorithmException, IOException;

    //用户确认故障
    void confirmTroubleWx(String openId, int orderNo) throws Exception;

    //用户拒绝确认故障
    void rejectTrouble(String openId, int orderNo) throws Exception;

    //维修完成
    void complete(int orderNo) throws IOException;

    //验收
    void checkAndAccept(int orderNo) throws ParseException;

    //查询历史订单
    List<Order> getHistoryOrder(String jobNo, String startTime, String endTime);

    //获取订单零件
    List<OrderPart> getOrderParts(int orderNo);

    //获取工程师确认故障详情
    OrderFault getOrderFault(int orderNo);

    //保存后台订单
    void saveBgOrder(BgOrder bgOrder);

    //保存申请的预约单
    void saveOrderAppointment(OrderAppointment orderAppointment);

    //修改预约时间
    void updateAppointmentTime(Timestamp appointmentTime, int orderNo);

    //根据单号查询预约信息
    OrderAppointment getAppointment(int orderNo);

    //更新订单信息
    void updateInfo(OrderAppointment orderAppointment);

    //获取订单详情
    OrderDetail getOrderDetail(int orderNo);

    //保存订单评价
    void orderEvaluate(OrderEvaluate orderEvaluate);

    //查询订单评价
    OrderEvaluate getOrderEvaluate(int orderNo);

    //查询某一天的排行(完成订单数量)
    List<Rank> getOrderCountRankByDate(@Param("date") String date) throws Exception;

    //根据平均分值排名
    List<Rank> getOrderScoreRankByDate(String date) throws Exception;

    //微信端获取订单详情(用户信息、工程师信息。。。)
    Order getOrder(int orderNo);

    //旧品回收
    List<OrderPart> getOldOrderPart(int orderNo);

    //用户成功付款调用
    boolean pay(int orderNo,int payType);

    //线下付款
    void offlinePay(int orderNo) throws IOException;

    //获取维修中的订单
    List<Order> getServiceOrders();

    //获取预约订单
    List<Order> getAppointmentOrders();

    //获取历史订单列表
    List<Order> getHistoryOrders();

    //待确认的售后订单列表
    List<Order> getAfterSaleOrders();

}
