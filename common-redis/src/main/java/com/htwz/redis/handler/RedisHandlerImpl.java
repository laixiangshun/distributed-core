package com.htwz.redis.handler;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisHandlerImpl implements RedisHandler {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 获取RedisTemplate
    @Override
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }
}