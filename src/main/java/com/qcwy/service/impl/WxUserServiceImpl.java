package com.qcwy.service.impl;

import com.qcwy.dao.*;
import com.qcwy.entity.*;
import com.qcwy.service.WxUserService;
import com.qcwy.utils.StringUtils;
import com.qcwy.utils.wx.WxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * Created by KouKi on 2017/2/16.
 */
@Service
@Transactional
public class WxUserServiceImpl implements WxUserService {
    @Autowired
    private WxUserDao wxUserDao;
    @Autowired
    private WxInfoDao wxInfoDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderCancelDao orderCancelDao;
    @Autowired
    private AppOrderMessageDao appOrderMessageDao;

    @Override
    public WxAccessToken getAccessToken(String code) throws IOException {
        WxInfo wxInfo = wxInfoDao.getInfo();
        return WxUtils.getAccessToken(wxInfo, code);
    }

    @Override
    public void register(WxUser wxUser) {
        wxUserDao.register(wxUser);
    }

    @Override
    public WxUser selectUserByOpenId(String openId) {
        return wxUserDao.selectUserByOpenId(openId);
    }

    @Override
    public Integer addTel(String tel, String openId) {
        return wxUserDao.addTel(tel, openId);
    }

    @Override
    public boolean hasUndoneOrder(String openId) {
        List<Order> orders = orderDao.getUndoneOrders(openId);
        if (orders.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public List<Order> getCurrentOrder(String openId) throws Exception {
        List<Order> order = orderDao.getUndoneOrders(openId);
        if (order == null || order.isEmpty()) {
            throw new Exception("当前没有未完成订单");
        }
        return order;
    }

    @Override
    public List<Order> getHistoryOrders(String openId) throws Exception {
        List<Order> orders = orderDao.getHistoryOrdersByOpenid(openId);
        if (orders == null || orders.isEmpty()) {
            throw new Exception("当前没有完成的订单");
        }
        return orders;
    }

    @Override
    public boolean getTel(String openId) {
        WxUser wxUser = wxUserDao.selectUserByOpenId(openId);
        if (StringUtils.isEmpty(wxUser.getTel())) {
            return false;
        }
        return true;
    }

    @Override
    public void cancelOrder(OrderCancel orderCancel) {
        orderCancelDao.save(orderCancel);
        //工程师端数据添加消息
        AppOrderMessage msg = new AppOrderMessage();
        msg.setJob_no(orderDao.getJobNoByOrderNo(orderCancel.getOrder_no()));
        msg.setType(1);
        msg.setOrder_no(orderCancel.getOrder_no());
        appOrderMessageDao.save(msg);
    }

    @Override
    public void updateWxUser(WxUser wxUser) {
        wxUserDao.updateWxUser(wxUser);
    }

    @Override
    public void updateLastRepairsTime(String openid) {
        wxUserDao.updateLastRepairsTime(openid);
    }

    @Override
    public List<WxUser> getWxUsers() {
        return wxUserDao.getWxUsers();
    }

}
