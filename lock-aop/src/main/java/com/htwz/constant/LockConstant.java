package com.htwz.constant;

public class LockConstant {
    public static final String LOCK_ENABLED = "lock.enabled";

    public static final String LOCK_TYPE = "lock.type";

    public static final String LOCK_TYPE_REDIS = "redisLock";
    public static final String LOCK_TYPE_ZOOKEEPER = "zookeeperLock";
    public static final String LOCK_TYPE_LOCAL = "localLock";

    public static final String LOCK_AOP_EXCEPTION_IGNORE = "lock.aop.exception.ignore";

    public static final String LOCK_SCAN_PACKAGES = "lock.scan.packages";
}