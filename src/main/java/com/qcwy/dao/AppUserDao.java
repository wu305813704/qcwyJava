package com.qcwy.dao;

import com.qcwy.entity.AppUser;
import org.apache.ibatis.annotations.*;
import org.mapstruct.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by KouKi on 2017/2/13.
 */

public interface AppUserDao {
    //获取工程师信息
    AppUser getUserByJobNo(@Param("jobNo") String jobNo);

    //添加用户
    void save(AppUser appUser);

    //登录
    AppUser login(@Param("jobNo") String job_no, @Param("pwd") String pwd);

    //查询所有在线工程师
    List<AppUser> findAllOnline();

    //工程师上传实时地理位置
    void updateLoc(@Param("lati") String lati, @Param("lon") String lon, @Param("updateTime") Timestamp updateTime, @Param("jobNo") String jobNo);

    //修改密码
    void updatePwd(@Param("pwd") String pwd, @Param("jobNo") String jobNo);

    //修改名字
    void updateName(@Param("name") String name, @Param("jobNo") String jobNo);

    //通过工号查询姓名
    String getUserName(@Param("jobNo") String jobNo);

}
