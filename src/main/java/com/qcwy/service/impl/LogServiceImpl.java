package com.qcwy.service.impl;

import com.qcwy.dao.bg.LogForAppDao;
import com.qcwy.dao.bg.LogForBgDao;
import com.qcwy.dao.bg.LogForWxDao;
import com.qcwy.entity.bg.LogForApp;
import com.qcwy.entity.bg.LogForBg;
import com.qcwy.entity.bg.LogForWx;
import com.qcwy.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by KouKi on 2017/5/4.
 */
@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogForWxDao logForWxDao;
    @Autowired
    private LogForAppDao logForAppDao;
    @Autowired
    private LogForBgDao logForBgDao;

    @Override
    public List<LogForWx> logForWxList() {
        return logForWxDao.getAll();
    }

    @Override
    public void saveLogWx(LogForWx logForWx) {
        logForWxDao.save(logForWx);
    }

    @Override
    public List<LogForApp> logForAppList() {
        return logForAppDao.getAll();
    }

    @Override
    public void saveLogApp(LogForApp logForApp) {
        logForAppDao.save(logForApp);
    }

    @Override
    public List<LogForBg> logForBgList() {
        return logForBgDao.getAll();
    }

    @Override
    public void saveLogBg(LogForBg logForBg) {
        logForBgDao.save(logForBg);
    }
}
