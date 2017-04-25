package com.qcwy.dao;

import com.qcwy.entity.OrderFault;
import org.apache.ibatis.annotations.Param;

/**
 * Created by KouKi on 2017/3/8.
 */
public interface OrderFaultDao {
    //保存故障详情
    void save(@Param("orderNo") int orderNo, @Param("faultId") String faultId, @Param("faultDescription") String faultDescription);

    //查询故障详情
    OrderFault getOrderFault(@Param("orderNo") int orderNo);
}
