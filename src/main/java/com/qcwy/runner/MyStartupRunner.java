package com.qcwy.runner;

import com.qcwy.dao.WxInfoDao;
import com.qcwy.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * Created by KouKi on 2017/4/17.
 * springboot 加载完毕后执行
 */
@Component
public class MyStartupRunner implements CommandLineRunner {
    private Jedis jedis = JedisUtil.getInstance();
    @Autowired
    private WxInfoDao dao;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println(dao.getInfo().getAppid());
    }
}
