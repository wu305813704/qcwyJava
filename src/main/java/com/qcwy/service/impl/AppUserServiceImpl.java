package com.qcwy.service.impl;

import com.qcwy.dao.*;
import com.qcwy.dao.bg.BgOrderDao;
import com.qcwy.entity.*;
import com.qcwy.model.Model;
import com.qcwy.service.AppUserService;
import com.qcwy.utils.MessageTypeUtils;
import com.qcwy.utils.ObjectMapperUtils;
import com.qcwy.utils.StringUtils;
import com.qcwy.websocket.BgWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by KouKi on 2017/2/16.
 */
@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {
    private Model model = new Model();
    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private OrderReassignmentDao orderReassignmentDao;
    @Autowired
    private AppOrderMessageDao appOrderMessageDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private BgOrderDao bgOrderDao;

    @Override
    public AppUser getUserByJobNo(String jobNo) {
        return appUserDao.getUserByJobNo(jobNo);
    }

    @Override
    public void save(AppUser appUser) {
        appUserDao.save(appUser);
    }

    @Override
    public AppUser login(String jobNo, String pwd) {

        return appUserDao.login(jobNo, pwd);
    }

    @Override
    public List<AppUser> findAllOnline() {
        return appUserDao.findAllOnline();
    }

    @Override
    public void updateLoc(String lati, String lon, String loc, String jobNo) throws IOException {
        appUserDao.updateLoc(lati, lon, loc, jobNo);
        //WebSocket推送给后台页面
        AppUser appUser = new AppUser();
        appUser.setJob_no(jobNo);
        appUser.setLati(lati);
        appUser.setLon(lon);
    }

    @Override
    public void updatePwd(String pwd, String jobNo) {
        appUserDao.updatePwd(pwd, jobNo);
    }

    @Override
    public void updateName(String name, String jobNo) {
        appUserDao.updateName(name, jobNo);
    }

    @Override
    public String getUserNmae(String jobNo) throws Exception {
        String name = appUserDao.getUserName(jobNo);
        if (StringUtils.isEmpty(name)) {
            throw new Exception("工号不存在");
        }
        return appUserDao.getUserName(jobNo);
    }

    //获取改派工程师姓名
    @Override
    public String getReassignmentName(int orderNo) throws Exception {
        String jobNo = orderReassignmentDao.getNewJobNo(orderNo);
        if (StringUtils.isEmpty(jobNo)) {
            if (bgOrderDao.getBgOrder(orderNo) == null) {
                throw new Exception("订单状态异常");
            } else {
                return "后台";
            }
        }
        String name = appUserDao.getUserName(jobNo);
        if (StringUtils.isEmpty(name)) {
            throw new Exception("工程师姓名不存在");
        }
        return name;
    }

    //通过工号查询订单消息
    @Override
    public List<AppOrderMessage> getOrderMsgByJobNo(String jobNo) {
        return appOrderMessageDao.getMsgByJobNo(jobNo);
    }

    //获取改派详情
    @Override
    public OrderReassignment getReassignment(int orderNo) {
        OrderReassignment orderReassignment = orderReassignmentDao.getReassignment(orderNo);
        String name = appUserDao.getUserName(orderReassignment.getOld_no());
        orderReassignment.setSendName(name);
        return orderReassignment;
    }

    //接受改派
    @Override
    public void acceptReassignment(Timestamp handleTime, int orderNo, String jobNo) {
        orderReassignmentDao.accept(handleTime, orderNo);
        //修改订单状态为1(接受)，工程师工号为新的工号
        orderDao.updateState(1, orderNo);
        orderDao.updateJobNo(jobNo, orderNo);
        //推送给被改派的工程师
        String name = appUserDao.getUserName(jobNo);
        String oldJobNo = orderReassignmentDao.getOldJobNo(orderNo);
        model.pushMsgToEnginner(oldJobNo, name, 1);
    }

    //拒绝改派
    @Override
    public void refuseReassignment(Timestamp handleTime, int orderNo, String jobNo) {
        orderReassignmentDao.refuse(handleTime, orderNo);
        //修改订单状态为2(拒绝)
        orderDao.updateState(2, orderNo);
        //推送给申请改派的工程师
        String name = appUserDao.getUserName(jobNo);
        String oldJobNo = orderReassignmentDao.getOldJobNo(orderNo);
        model.pushMsgToEnginner(oldJobNo, name, 2);
    }

    @Override
    public int getOrderCount(String jobNo) {
        return orderDao.getOrderCount(jobNo);
    }

    @Override
    public void updateUser(AppUser appUser) {
        appUserDao.updateUser(appUser);
    }

    @Override
    public void delete(String jobNo) {
        appUserDao.delete(jobNo);
    }
}
