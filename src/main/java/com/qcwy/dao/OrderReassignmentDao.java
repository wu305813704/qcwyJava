package com.qcwy.dao;

import com.qcwy.entity.OrderReassignment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mapstruct.Mapper;

import java.sql.Timestamp;

/**
 * Created by KouKi on 2017/2/22.
 */
@Mapper
public interface OrderReassignmentDao {
    //添加改派信息
    void save(@Param("reassignment") OrderReassignment reassignment);

    //获取改派详情
    OrderReassignment getReassignment(@Param("orderNo") int orderNo);

    //查询被改派人工号
    String getNewJobNo(@Param("orderNo") int orderNo);

    //查询申请改派人工号
    String getOldJobNo(@Param("orderNo") int orderNo);

    //接受改派
    void accept(@Param("handleTime") Timestamp handleTime, @Param("orderNo") int orderNo);

    //拒绝改派
    void refuse(@Param("handleTime") Timestamp handleTime, @Param("orderNo") int orderNo);
}
