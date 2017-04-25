package com.qcwy.dao.bg;

import com.qcwy.entity.bg.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KouKi on 2017/3/29.
 */
public interface MenuDao {

    List<Menu> getMenusByParentId(@Param("id") int id);

    //通过角色id查询所有菜单
    List<Menu> getMenusByRoleid(@Param("roleId") int roleId);
}
