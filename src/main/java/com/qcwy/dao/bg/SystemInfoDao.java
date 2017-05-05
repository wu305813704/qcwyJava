package com.qcwy.dao.bg;

import com.qcwy.entity.bg.SystemInfo;
import org.apache.ibatis.annotations.Param;

/**
 * Created by KouKi on 2017/5/4.
 */
public interface SystemInfoDao {
    //更新系统信息
    void update(@Param("sys") SystemInfo sys);
}
