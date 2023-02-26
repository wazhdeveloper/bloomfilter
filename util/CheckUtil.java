package com.redis.bloom.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author wazh
 * @description 使用布隆过滤器检查key是否存在
 * @since 2023-02-25-21:19
 */
@Slf4j
public class CheckUtil {

    @Resource
    private static RedisTemplate redisTemplate;

    public static boolean checkKeyExistInBloom(String key, String bloomName) {
        int abs = Math.abs(key.hashCode());
        long index = (long) (abs % Math.pow(2,32));
        Boolean exist = redisTemplate.opsForValue().getBit(key, index);
        log.info("key: {}, 对应的index: {}, 是否存在: {}", key, index, exist);
        return exist;
    }
}
