package com.qcwy.dao.bg;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KouKi on 2017/5/9.
 */
public interface RoleMenuDao {
    //批量添加
    void addRoleMenu(@Param("roleId") int roleId, @Param("menuIds") List<Integer> menuIds);
}
