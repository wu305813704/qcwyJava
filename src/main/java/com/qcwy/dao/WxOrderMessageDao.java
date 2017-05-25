package com.qcwy.dao;

import com.qcwy.entity.WxOrderMessage;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Created by KouKi on 2017/2/23.
 */
@Mapper
public interface WxOrderMessageDao {
    //添加消息
    void save(@Param("msg") WxOrderMessage msg);

    //通过openid查询订单消息(时间降序)
    List<WxOrderMessage> getMsgByOpenid(@Param("openid") String openid);

}
