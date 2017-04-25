package com.qcwy.dao.bg;

import com.qcwy.entity.bg.BgOrder;
import org.apache.ibatis.annotations.Param;

/**
 * Created by KouKi on 2017/3/16.
 */
public interface BgOrderDao {

    //保存
    void save(@Param("bgOrder") BgOrder bgOrder);

    //查询
    BgOrder getBgOrder(@Param("orderNo") int orderNo);
}
