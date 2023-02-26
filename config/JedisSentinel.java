package com.redis.bloom.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;

/**
 * @author wzhstart
 * @creat 2022-09-02-9:47
 */
public class JedisSentinel {

    private static JedisSentinelPool jedisSentinelPool;

    public Jedis getJedisSentinel() {
        if (jedisSentinelPool == null) {
            HashSet<String> sentinelConfig = new HashSet<>();
            sentinelConfig.add("192.168.88.100:26379");
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(10);
            jedisPoolConfig.setMaxIdle(5);
            jedisPoolConfig.setMinIdle(5);
            jedisPoolConfig.setBlockWhenExhausted(true);
            jedisPoolConfig.setMaxWaitMillis(12000);
            jedisPoolConfig.getTestOnBorrow();
            JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("mymaster",sentinelConfig,jedisPoolConfig);
            return jedisSentinelPool.getResource();
        }
        return jedisSentinelPool.getResource();
    }
}
