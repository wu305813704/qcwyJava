package com.qcwy.dao;

import com.qcwy.entity.OrderDetail;
import org.apache.ibatis.annotations.*;

/**
 * Created by KouKi on 2017/2/21.
 */
public interface OrderDetailDao {
    //保存订单明细
    void saveOrderDetail(@Param("order_no") int orderNo, @Param("orderDetail") OrderDetail orderDetail);
    //根据订单号查询订单详情
    OrderDetail getOrderDetail(@Param("orderNo") int orderNo);

    //加价
    void addPrice(@Param("addPrice") int addPrice, @Param("orderNo") int orderNo);

    void updateOrderInfo(@Param("orderNo") int orderNo, @Param("lon") String lon, @Param("lati") String lati, @Param("loc") String loc);

    //计算总价
    void updateTotalPrice(@Param("totalPrice") double totalPrice);
}
