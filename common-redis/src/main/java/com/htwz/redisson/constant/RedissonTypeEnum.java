package com.htwz.redisson.constant;

import lombok.Getter;

/**
 * redisson类型枚举
 *
 * @author lxs
 */
@Getter
public enum RedissonTypeEnum {
    /**
     * 哨兵模式
     */
    sentinel("sentinel"),
    /**
     * 单机模式
     */
    stand_alone("stand-alone"),
    /**
     * 集群
     */
    cluster("cluster");

    private String name;

    RedissonTypeEnum(String name) {
        this.name = name;
    }

    public static RedissonTypeEnum getRedissonType(String name) {
        for (RedissonTypeEnum redissonTypeEnum : RedissonTypeEnum.values()) {
            if (redissonTypeEnum.getName().equals(name)) {
                return redissonTypeEnum;
            }
        }
        return null;
    }
}
