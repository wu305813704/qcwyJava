package com.qcwy.dao.bg;

import com.qcwy.entity.bg.OrderVisit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KouKi on 2017/5/4.
 */
public interface OrderVisitDao {
    //获取已回访列表
    List<OrderVisit> getAllList();

    //保存回访记录
    void save(@Param("userNo") String userNo, @Param("orderNo") int orderNo, @Param("content") String content);

    //修改回访状态
    void updateReturnState(@Param("orderNo") int orderNo);
}
