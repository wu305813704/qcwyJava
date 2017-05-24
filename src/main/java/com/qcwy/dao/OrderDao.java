package com.qcwy.dao;

import com.qcwy.entity.Order;
import com.qcwy.entity.Rank;
import org.apache.ibatis.annotations.Param;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by KouKi on 2017/2/14.
 */

public interface OrderDao {
    //根据单号获取OpenId
    String getOpenIdByOrderNo(@Param("orderNo") int orderNo);

    //通过单号获取订单
    Order getOrderByOrderNo(@Param("orderNo") int orderNo);

    //查询所有未抢订单
    List<Order> findFreeOrders();

    //下普通订单
    void saveOrder(@Param("order") Order order);

    //预约单
    void appointmentOrder(@Param("order") Order order);

    //更新预约时间
    void updateAppointmentTime(@Param("appointmentTime") Timestamp appointmentTime, @Param("orderNo") int orderNo);

    //查询订单状态
    Integer getStateByNo(@Param("orderNo") int orderNo);

    //修改订单状态
    void updateState(@Param("state") int state, @Param("orderNo") int orderNo);

    //修改工程师工号
    void updateJobNo(@Param("jobNo") String jobNo, @Param("orderNo") int orderNo);

    //抢单
    void rushOrder(@Param("jobNo") String jobNo, @Param("orderNo") int orderNo);

    //查询工程师所持订单
    List<Order> getHoldOrders(@Param("jobNo") String jobNo);

    //查询工程师所持订单数量
    int getCountHoldOrders(@Param("jobNo") String jobNo);

    //开始订单
    void startOrder(@Param("orderNo") int orderNo);

    //暂停订单
    void pauseOrder(@Param("orderNo") int orderNo);

    //通过订单号查询工程师工号
    String getJobNoByOrderNo(@Param("orderNo") int orderNo);

    //改派订单
    void reassignmentOrder(@Param("orderNo") int orderNo);

    //查询工程师历史订单
    List<Order> getHistoryOrdersByJobNo(@Param("jobNo") String jobNo, @Param("startTime") String startTime, @Param("endTime") String endTime);

    //更新完成时间
    void updateCompleteTime(@Param("orderNo") int orderNo);

    //查询某一天的排行(完成订单数量)
    List<Rank> getOrderCountRank(@Param("date") String date);

    //根据平均分值排名
    List<Rank> getOrderScoreRank(@Param("date") String date);

    //查询用户是否有未完成的订单
    List<Order> getUndoneOrders(@Param("openId") String openId);

    //查询用户是否有未完成的订单
    List<Order> getHistoryOrdersByOpenid(@Param("openId") String openId);

    //获取订单明细
    Order getOrder(@Param("orderNo") int orderNo);

    //付款
    void pay(@Param("orderNo") int orderNo, @Param("payType") int payType);

    //获取维修中的订单
    List<Order> getServiceOrders();

    //获取预约订单
    List<Order> getAppointmentOrders();

    //获取历史订单
    List<Order> getHistoryOrders();

    //待确认的售后订单
    List<Order> getAfterSaleOrders();

    //获取订单数量
    int getOrderCount(@Param("jobNo") String jobNo);

    //获取所有订单列表
    List<Order> getAllOrders();

    //发起售后订单
    void afterSaleOrder(@Param("order") Order order);

    //获取回访列表
    List<Order> getReturnVisitList();

    //派发订单给工程师
    void setJobNo(@Param("orderNo") Integer orderNo, @Param("jobNo") String jobNo);

    //修改回访状态未已回访
    void updateReturnState(@Param("orderNo") int orderNo);

    //查询历史售后订单
    List<Order> getHistoryAfterSale();

    //设置订单类型
    void updateType(@Param("orderNo") Integer orderNo, @Param("type") Integer type);

    //根据日期查询订单
    List<Order> getOrderByDate(@Param("date") String date);

    //根据工号查询订单
    List<Order> getOrderByJobNo(@Param("jobNo") String jobNo);

    //根据来源(微信)获取订单
    List<Order> getOrderByWx();

    //根据来源(后台)获取订单
    List<Order> getOrderByBg();

    //根据客户电话获取订单
    List<Order> getOrderByWxTel();
}
