package com.qcwy.dao.bg;

import com.qcwy.entity.bg.LogForApp;
import com.qcwy.entity.bg.LogForBg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by KouKi on 2017/5/4.
 */
public interface LogForBgDao {
    //获取所有日志
    List<LogForBg> getAll();

    //保存日志
    void save(@Param("log") LogForBg logForBg);
}
