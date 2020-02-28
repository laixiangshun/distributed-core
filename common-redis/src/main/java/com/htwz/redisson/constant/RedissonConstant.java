package com.htwz.redisson.constant;

public class RedissonConstant {

    public static final String DEFAULT_PATH = "redisson.yaml";

    public static final String PATH = "redisson.path";
    public static final String TIMEOUT = "redisson.timeout";
    public static final String ADDRESS = "redisson.address";
    public static final String PASSWORD = "redisson.password";
    public static final String DATABASE = "redisson.database";
    public static final String CONNECTIONPOOLSIZE = "redisson.connectionPoolSize";
    public static final String CONNECTIONMINIMUMIDLESIZE = "redisson.connectionMinimumIdleSize";
    public static final String SLAVECONNECTIONPOOLSIZE = "redisson.slaveConnectionPoolSize";
    public static final String MASTERCONNECTIONPOOLSIZE = "redisson.masterConnectionPoolSize";
    public static final String SENTINELADDRESSES = "redisson.sentinelAddresses";
    public static final String MASTERNAME = "redisson.masterName";
    /**
     * redisson类型，支持单机，哨兵模式
     * 单机：stand-alone
     * 哨兵模式：sentinel
     */
    public static final String REDISTYPE = "redisson.type";

}