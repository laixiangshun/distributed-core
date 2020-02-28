package com.htwz.redisson.adapter;

import com.htwz.redisson.handler.RedissonHandler;

/**
 * redisson 适配器
 *
 * @author lxs
 */
public interface RedissonAdapter {

    /**
     * 获取redisson 处理器
     *
     * @return
     */
    RedissonHandler getRedissonHandler();
}