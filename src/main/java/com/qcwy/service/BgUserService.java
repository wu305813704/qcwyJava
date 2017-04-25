package com.qcwy.service;

import com.qcwy.entity.PartDetail;
import com.qcwy.entity.Role;
import com.qcwy.entity.bg.BgUser;
import com.qcwy.entity.bg.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by KouKi on 2017/3/24.
 */
public interface BgUserService {
    //添加用户
    void addUser(BgUser bgUser);

    //登录
    List<Role> login(String username, String pwd) throws Exception;

    //添加角色
    void addRole(Role role);

    //删除角色
    void deleteRole(int id);

    //修改角色
    void updateRole(Role role);

    //通过Id查询
    Role selectRoleById(int id) throws Exception;

    //查询所有角色
    List<Role> selectAllRole() throws Exception;

    //查询所有菜单
    List<Menu> selectAllMenu();

    //通过角色id查询拥有的菜单
    List<Menu> getMenuByRoleId(int roleId);

    //修改价格
    void updatePartPrice(PartDetail partDetail, String username);

    //查询所有员工
    List<BgUser> getBgUserList();
}