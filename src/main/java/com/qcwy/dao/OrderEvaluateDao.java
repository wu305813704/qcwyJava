package com.qcwy.dao;

import com.qcwy.entity.OrderEvaluate;
import org.apache.ibatis.annotations.Param;

/**
 * Created by KouKi on 2017/3/21.
 */
public interface OrderEvaluateDao {
    //保存评价
    void save(@Param("orderEvaluate") OrderEvaluate orderEvaluate);

    //查询评价
    OrderEvaluate getOrderEvaluate(@Param("orderNo") int orderNo);
}
