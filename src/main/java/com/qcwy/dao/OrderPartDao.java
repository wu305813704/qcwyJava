package com.qcwy.dao;

import com.qcwy.entity.OrderPart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KouKi on 2017/3/8.
 */
public interface OrderPartDao {
    //保存订单零件
    void save(@Param("orderNo") int orderNo, @Param("partDetailId") int partDetailId, @Param("count")
            int count, @Param("type") int type);

    //获取订单的零件
    List<OrderPart> getOrderParts(@Param("orderNo") int orderNo);

    List<OrderPart> getOldOrderPart(int orderNo);
}
