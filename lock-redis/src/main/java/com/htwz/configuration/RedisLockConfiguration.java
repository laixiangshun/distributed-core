package com.htwz.configuration;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.htwz.LockDelegate;
import com.htwz.LockExecutor;
import com.htwz.condition.RedisLockCondition;
import com.htwz.impl.RedisLockDelegateImpl;
import com.htwz.impl.RedisLockExecutorImpl;
import com.htwz.redisson.adapter.RedissonAdapter;
import com.htwz.redisson.constant.RedissonConstant;
import com.htwz.redisson.constant.RedissonTypeEnum;
import com.htwz.redisson.handler.RedissonHandler;
import com.htwz.redisson.handler.RedissonHandlerImpl;
import com.htwz.redisson.properties.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * redisson配置
 *
 * @author lxs
 */
@Slf4j
@Configuration
@ConditionalOnClass(Config.class)
@Conditional(RedisLockCondition.class)
public class RedisLockConfiguration {

    @Autowired(required = false)
    private RedissonAdapter redissonAdapter;

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    @Conditional(RedisLockCondition.class)
    public LockDelegate redisLockDelegate() {
        return new RedisLockDelegateImpl();
    }

    @Bean
    @Conditional(RedisLockCondition.class)
    public LockExecutor<RLock> redisLockExecutor() {
        return new RedisLockExecutorImpl();
    }

    @Value("${" + RedissonConstant.PATH + "}")
    private String yamlConfigPath;

    @Bean
    @Conditional(RedisLockCondition.class)
    @ConditionalOnMissingBean
    public RedissonProperties redissonProperties() {
        RedissonProperties.RedissonPropertiesBuilder builder = RedissonProperties.builder()
                .address("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
                .connectionMinimumIdleSize(redisProperties.getJedis().getPool().getMinIdle())
                .connectionPoolSize(redisProperties.getJedis().getPool().getMaxIdle())
                .database(redisProperties.getDatabase())
                .password(redisProperties.getPassword())
                .timeout(Math.toIntExact(redisProperties.getTimeout().toMillis()));
        if (StrUtil.isNotBlank(yamlConfigPath)) {
            builder.path(yamlConfigPath);
        }

        builder.type(RedissonTypeEnum.stand_alone.getName());

        RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
        if (ObjectUtil.isNotNull(sentinel)) {
            if (StrUtil.isBlank(redisProperties.getSentinel().getMaster())) {
                log.error("哨兵模式的redisson配置：缺少 Master server name");
                System.exit(-1);
            }
            List<String> nodes = redisProperties.getSentinel().getNodes();
            nodes = nodes.stream().map(node -> "redis://" + node).collect(Collectors.toList());
            builder.masterName(redisProperties.getSentinel().getMaster())
                    .sentinelAddresses(nodes.toArray(new String[0]))
                    .masterConnectionPoolSize(redisProperties.getJedis().getPool().getMaxIdle())
                    .slaveConnectionPoolSize(redisProperties.getJedis().getPool().getMaxIdle())
                    .type(RedissonTypeEnum.sentinel.getName());
        }
        RedisProperties.Cluster cluster = redisProperties.getCluster();
        if (ObjectUtil.isNotNull(cluster)) {
            if (CollUtil.isEmpty(redisProperties.getCluster().getNodes())) {
                log.error("集群模式的redisson配置：缺少节点，list of cluster nodes and is required to have at least one entry");
                System.exit(-1);
            }
            List<String> nodes = redisProperties.getCluster().getNodes();
            nodes = nodes.stream().map(node -> "redis://" + node).collect(Collectors.toList());
            builder.clusterNodes(nodes.toArray(new String[0]))
                    .maxRedirects(redisProperties.getCluster().getMaxRedirects())
                    .type(RedissonTypeEnum.cluster.getName());
        }
        return builder.build();
    }

    @Bean
    @Conditional(RedisLockCondition.class)
    @ConditionalOnMissingBean
    public RedissonHandler redissonHandler() {
        if (redissonAdapter != null) {
            return redissonAdapter.getRedissonHandler();
        }

        return new RedissonHandlerImpl(redissonProperties());
    }
}