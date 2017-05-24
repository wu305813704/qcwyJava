package com.qcwy.service;

import com.qcwy.entity.Order;
import com.qcwy.entity.OrderCancel;
import com.qcwy.entity.WxAccessToken;
import com.qcwy.entity.WxUser;

import java.io.IOException;
import java.util.List;

/**
 * Created by KouKi on 2017/2/16.
 */

public interface WxUserService {

    //获取access_token
    WxAccessToken getAccessToken(String code) throws IOException;

    //注册
    void register(WxUser wxUser);

    //查询
    WxUser selectUserByOpenId(String openId);

    //添加手机号
    Integer addTel(String tel, String openId);

    //查询是否有为完成的订单
    boolean hasUndoneOrder(String openId);

    //查询用户当前订单
    List<Order> getCurrentOrder(String openId) throws Exception;

    //查询用户历史订单
    List<Order> getHistoryOrders(String openId) throws Exception;

    //查询电话号码是否存在
    boolean getTel(String openId);

    //取消订单
    void cancelOrder(OrderCancel orderCancel);

    //更新微信用户信息
    void updateWxUser(WxUser wxUser);

    //更新最后保修时间
    void updateLastRepairsTime(String openid);

    //用户列表
    List<WxUser> getWxUsers();

    //拉黑
    void block(String openid);

    //取消拉黑
    void rejectBlock(String openid);

    //黑名单列表
    List<WxUser> getBlacklist();

}
