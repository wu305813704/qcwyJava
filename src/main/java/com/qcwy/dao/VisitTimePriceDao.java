package com.qcwy.dao;

import com.qcwy.entity.VisitTimePrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KouKi on 2017/3/28.
 */
public interface VisitTimePriceDao {
    //查询所有对应上门费
    List<VisitTimePrice> getAllTimePrice();

    void setTimePrice(@Param("timePrice") VisitTimePrice timePrice);
}
