package com.qcwy.dao.bg;

import com.qcwy.entity.bg.BgUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KouKi on 2017/3/29.
 */
public interface BgUserDao {

    //登录
    BgUser login(@Param("username") String username, @Param("pwd") String pwd);

    //获取用户列表
    List<BgUser> getBgUserList();

    //添加用户
    void addUser(@Param("user") BgUser bgUser);

    //判断是否存在用户名
    boolean hasUsername(@Param("username") String username);
}
