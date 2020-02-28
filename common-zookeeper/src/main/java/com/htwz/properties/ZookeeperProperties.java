package com.htwz.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * zookeeper 配置
 *
 * @author lxs
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ZookeeperProperties {

    private String connectString;

    private int sessionTimeoutMs;

    private int connectionTimeoutMs;

    private String retryType;

    private int exponentialBackoffRetryBaseSleepTimeMs;

    private int exponentialBackoffRetryMaxRetries;

    private int boundedExponentialBackoffRetryBaseSleepTimeMs;

    private int boundedExponentialBackoffRetryMaxSleepTimeMs;

    private int boundedExponentialBackoffRetryMaxRetries;

    private int retryTimesCount;

    private int retryTimesSleepMsBetweenRetries;

    private int retryForeverRetryIntervalMs;

    private int retryUntilElapsedMaxElapsedTimeMs;

    private int retryUntilElapsedSleepMsBetweenRetries;

}
