package com.redis.bloom.filter;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author wazh
 * @description 布隆过滤器
 * @since 2023-02-25-21:07
 */
@Slf4j
@Component
public class Bloomfilter {

    private static final String WHITE_NAME_LIST="whitenamelist";
    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation("将key添加到bitmap中")
    public void init(int customerId) {
        String key = "customer:" + customerId;
        int abs = Math.abs(key.hashCode());
        long sit = (long)(abs % Math.pow(2,32));
        log.info("key={},对应的站位为{}",key, sit);
        redisTemplate.opsForValue().setBit(WHITE_NAME_LIST, sit, true);
    }
}
