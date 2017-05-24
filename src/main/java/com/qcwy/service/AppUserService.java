package com.qcwy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qcwy.entity.AppOrderMessage;
import com.qcwy.entity.AppUser;
import com.qcwy.entity.OrderReassignment;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by KouKi on 2017/2/16.
 */

public interface AppUserService {
    //获取工程师信息
    AppUser getUserByJobNo(String jobNo);

    //添加用户
    void save(AppUser appUser);

    //登录
    AppUser login(String jobNo, String pwd);

    //查询所有在线工程师
    List<AppUser> findAllOnline();

    //工程师上传实时地理位置
    void updateLoc(String lati, String lon, String loc, String jobNo) throws IOException;

    //修改密码
    void updatePwd(String pwd, String jobNo);

    //修改名字
    void updateName(String name, String jobNo);

    //通过工号查询姓名
    String getUserNmae(String jobNo) throws Exception;

    //查询改约单工程师姓名
    String getReassignmentName(int orderNo) throws Exception;

    //通过工号查询订单消息
    List<AppOrderMessage> getOrderMsgByJobNo(String jobNo);

    //获取改派详情
    OrderReassignment getReassignment(int orderNo);

    //接受改派
    void acceptReassignment(Timestamp handleTime, int orderNo, String jobNo);

    //拒绝改派
    void refuseReassignment(Timestamp handleTime, int orderNo, String jobNo);

    //获取订单数量
    int getOrderCount(String jobNo);

    //修改工程师
    void update(AppUser appUser);
}
