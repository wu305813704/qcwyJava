package com.qcwy.dao;

import com.qcwy.entity.AppOrderMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Created by KouKi on 2017/2/23.
 */
@Mapper
public interface AppOrderMessageDao {
    //添加消息
    void save(@Param("msg") AppOrderMessage msg);

    //通过工号查询订单消息(时间降序)
    List<AppOrderMessage> getMsgByJobNo(@Param("jobNo") String jobNo);
}
