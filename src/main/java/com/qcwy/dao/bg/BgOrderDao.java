package com.qcwy.dao.bg;

import com.qcwy.entity.bg.BgOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KouKi on 2017/3/16.
 */
public interface BgOrderDao {

    //保存
    void save(@Param("bgOrder") BgOrder bgOrder);

    //通过订单号查询
    BgOrder getBgOrder(@Param("orderNo") int orderNo);

    //获取所有改派订单
    List<BgOrder> getAllRessignmentOrders();

    //获取所有超时点单
    List<BgOrder> getAllOverTimeOrders();

    //获取待派发订单
    List<BgOrder> getDistributeOrders();

    //修改订单状态为已派发
    void updateState(@Param("orderNo") Integer orderNo, @Param("state") Integer state);
}
