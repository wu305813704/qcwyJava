package com.qcwy.dao;

import com.qcwy.entity.WxUser;
import org.apache.ibatis.annotations.*;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Created by KouKi on 2017/2/13.
 */
@Mapper
public interface WxUserDao {
    //注册
    void register(@Param("wxUser") WxUser wxUser);

    //查询
    WxUser selectUserByOpenId(@Param("openId") String openId);

    //添加手机号
    Integer addTel(@Param("tel") String tel, @Param("openId") String openId);

    //更新微信用户信息
    void updateWxUser(@Param("wxUser") WxUser wxUser);

    //更新最后报修时间
    void updateLastRepairsTime(@Param("openid") String openid);

    //微信用户列表
    List<WxUser> getWxUsers();

    //拉黑
    void block(@Param("openid") String openid);

    //取消拉黑
    void rejectBlock(@Param("openid") String openid);

    //黑名单列表
    List<WxUser> getBlacklist();
}
