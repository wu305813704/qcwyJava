package com.qcwy.dao.bg;

import com.qcwy.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KouKi on 2017/3/24.
 */
public interface BgRoleDao {
    //添加角色
    void add(@Param("role") Role role);

    //删除角色
    void delete(@Param("id") int id);

    //修改角色
    void update(@Param("roleId") Integer roleId, @Param("roleName") String roleName);

    //通过Id查询
    Role selectById(@Param("id") int id);

    //查询所有
    List<Role> selectAll();
}
