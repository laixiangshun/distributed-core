package com.htwz.redisson.properties;

import lombok.Builder;
import lombok.Data;

/**
 * redisson 配置属性
 *
 * @author lxs
 */
@Data
@Builder
//@Configuration
//@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {

    @Builder.Default
    private int timeout = 3000;
    @Builder.Default
    private String address = "localhost:6379";

    private String password;
    @Builder.Default
    private int database = 0;
    @Builder.Default
    private int connectionPoolSize = 64;
    @Builder.Default
    private int connectionMinimumIdleSize = 10;
    @Builder.Default
    private int slaveConnectionPoolSize = 250;
    @Builder.Default
    private int masterConnectionPoolSize = 250;

    /**
     * 哨兵模式地址
     */
    private String[] sentinelAddresses;

    /**
     * 在设置哨兵模式时设置
     */
    private String masterName;

    /**
     * redisson配置文件路径
     */
    private String path;

    /**
     * redisson类型，支持单机，哨兵模式,集群模式
     * * 单机：stand-alone
     * * 哨兵模式：sentinel
     */
    private String type;
    /**
     * Maximum number of redirects to follow when executing commands across the
     * cluster.
     */
    private Integer maxRedirects;

    /**
     * 集群模式地址
     */
    private String[] clusterNodes;
}
