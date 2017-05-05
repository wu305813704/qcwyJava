package com.qcwy.dao.bg;

import com.qcwy.entity.bg.LogForWx;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KouKi on 2017/5/4.
 */
public interface LogForWxDao {
    //获取所有日志
    List<LogForWx> getAll();

    //保存日志
    void save(@Param("log") LogForWx logForWx);
}
