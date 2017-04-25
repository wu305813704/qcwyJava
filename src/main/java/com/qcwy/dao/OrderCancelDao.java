package com.qcwy.dao;

import com.qcwy.entity.OrderCancel;
import org.apache.ibatis.annotations.Param;

/**
 * Created by KouKi on 2017/4/12.
 * 取消订单原因记录
 */
public interface OrderCancelDao {

    void save(@Param("orderCancel") OrderCancel orderCancel);
}
