package com.qcwy.dao.bg;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KouKi on 2017/3/29.
 */
public interface UserRoleDao {
    //通过userid查询role
    List<Integer> getRoleIdsByUserId(@Param("userId") int userId);
}
