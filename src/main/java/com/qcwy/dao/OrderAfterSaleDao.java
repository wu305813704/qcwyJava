package com.qcwy.dao;

import org.apache.ibatis.annotations.Param;

/**
 * Created by KouKi on 2017/5/3.
 */
public interface OrderAfterSaleDao {
    //保存驳回原因
    void save(@Param("orderNo") int orderNo, @Param("cause") String cause);
}
