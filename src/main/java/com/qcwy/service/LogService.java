package com.qcwy.service;

import com.qcwy.entity.bg.LogForApp;
import com.qcwy.entity.bg.LogForBg;
import com.qcwy.entity.bg.LogForWx;

import java.util.List;

/**
 * Created by KouKi on 2017/5/4.
 */
public interface LogService {
    //微信用户日志
    List<LogForWx> logForWxList();

    //保存微信用户日志
    void saveLogWx(LogForWx logForWx);

    //工程师日志
    List<LogForApp> logForAppList();

    //保存工程师日志
    void saveLogApp(LogForApp logForApp);

    //后台用户日志
    List<LogForBg> logForBgList();

    //保存工程师日志
    void saveLogBg(LogForBg logForBg);
}
