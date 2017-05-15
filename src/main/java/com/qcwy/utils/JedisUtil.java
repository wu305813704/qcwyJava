//package com.qcwy.utils;
//
//import redis.clients.jedis.Jedis;
//
///**
// * Created by KouKi on 2017/4/21.
// */
//public class JedisUtil {
//    private static Jedis jedis = null;
//
//    public synchronized static Jedis getInstance() {
//        if (jedis == null) {
//            return new Jedis("127.0.0.1", 6379);
//        } else {
//            return jedis;
//        }
//    }
//
//}
