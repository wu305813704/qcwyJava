package com.qcwy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by KouKi on 2017/5/14.
 */
@Component
public class RedisClient {
    @Autowired
    private JedisPool jedisPool;

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    public void expire(String key, int validity) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.expire(key, validity);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    public void del(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    public void setex(String token, int validity, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setex(token,validity,key);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

}
