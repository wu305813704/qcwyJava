package com.qcwy.runner;

import com.qcwy.RedisClient;
import com.qcwy.dao.WxInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by KouKi on 2017/4/17.
 * springboot 加载完毕后执行
 */
@Component
public class MyStartupRunner implements CommandLineRunner {
    @Autowired
    private RedisClient jedis;
    @Autowired
    private WxInfoDao dao;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println(dao.getInfo().getAppid());
    }
}
