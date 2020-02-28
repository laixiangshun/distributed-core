package com.htwz.redisson.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.htwz.redisson.constant.RedissonTypeEnum;
import com.htwz.redisson.exception.RedissonException;
import com.htwz.redisson.properties.RedissonProperties;
import com.htwz.redisson.util.RedissonUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.htwz.redisson.constant.RedissonConstant.DEFAULT_PATH;

/**
 * redisson 分布式锁处理器
 *
 * @author lxs
 */
public class RedissonHandlerImpl implements RedissonHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RedissonHandlerImpl.class);

    private RedissonClient redisson;

    public RedissonHandlerImpl(RedissonProperties redissonProperties) {
        try {
            String type = redissonProperties.getType();
            RedissonTypeEnum redissonType = RedissonTypeEnum.getRedissonType(type);
            if (ObjectUtil.isNull(redissonType) && StrUtil.isNotBlank(redissonProperties.getPath())) {
                create(redissonProperties.getPath());
            } else {
                create(redissonProperties);
            }
        } catch (Exception e) {
            LOG.error("Initialize Redisson failed", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public void create(String yamlConfigPath) throws IOException {
        if (StrUtil.isBlank(yamlConfigPath)) {
            yamlConfigPath = DEFAULT_PATH;
        }
        Config config = RedissonUtil.createYamlFileConfig(yamlConfigPath);
        initialize(config);
    }

    public RedissonHandlerImpl(Config config) {
        try {
            initialize(config);
        } catch (Exception e) {
            LOG.error("Initialize Redisson failed", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 创建Redisson
     */
    private void initialize(Config config) {
        create(config);
    }

    /**
     * 根据属性配置初始化redisson client
     */
    private void create(RedissonProperties redissonProperties) {
        String type = redissonProperties.getType();
        if (RedissonTypeEnum.sentinel.getName().equals(type)) {
            //哨兵模式
            Config config = new Config();
            SentinelServersConfig serverConfig = config.useSentinelServers()
                    .addSentinelAddress(redissonProperties.getSentinelAddresses())
                    .setMasterName(redissonProperties.getMasterName())
                    .setTimeout(redissonProperties.getTimeout())
                    .setDatabase(redissonProperties.getDatabase())
                    .setMasterConnectionPoolSize(redissonProperties.getMasterConnectionPoolSize())
                    .setSlaveConnectionPoolSize(redissonProperties.getSlaveConnectionPoolSize());

            if (StringUtils.isNotBlank(redissonProperties.getPassword())) {
                serverConfig.setPassword(redissonProperties.getPassword());
            }
            this.redisson = Redisson.create(config);
        } else if (RedissonTypeEnum.stand_alone.getName().equals(type)) {
            //单机模式
            Config config = new Config();
            SingleServerConfig serverConfig = config.useSingleServer()
                    .setAddress(redissonProperties.getAddress())
                    .setTimeout(redissonProperties.getTimeout())
                    .setDatabase(redissonProperties.getDatabase())
                    .setConnectionPoolSize(redissonProperties.getConnectionPoolSize())
                    .setConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize());

            if (StringUtils.isNotBlank(redissonProperties.getPassword())) {
                serverConfig.setPassword(redissonProperties.getPassword());
            }
            this.redisson = Redisson.create(config);
        } else if (RedissonTypeEnum.cluster.getName().equals(type)) {
            //集群模式
            Config config = new Config();
            ClusterServersConfig serversConfig = config.useClusterServers()
                    .addNodeAddress(redissonProperties.getClusterNodes())
                    .setTimeout(redissonProperties.getTimeout())
                    .setMasterConnectionPoolSize(redissonProperties.getMasterConnectionPoolSize())
                    .setSlaveConnectionPoolSize(redissonProperties.getSlaveConnectionPoolSize())
                    .setConnectTimeout(redissonProperties.getTimeout())
                    .setIdleConnectionTimeout(redissonProperties.getTimeout())
                    .setMasterConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize())
                    .setSlaveConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize());
            if (StringUtils.isNotBlank(redissonProperties.getPassword())) {
                serversConfig.setPassword(redissonProperties.getPassword());
            }
            this.redisson = Redisson.create(config);
        } else {
            LOG.error("redisson client init failed,no know redisson type: {}", type);
        }
        LOG.info("redisson client init success!");
    }

    /**
     * 使用config创建Redisson
     */
    private void create(Config config) {
        LOG.info("Start to initialize Redisson...");

        if (redisson != null) {
            throw new RedissonException("Redisson isn't null, it has been initialized already");
        }

        redisson = Redisson.create(config);
    }

    /**
     * 关闭Redisson客户端连接
     */
    @Override
    public void close() {
        LOG.info("Start to close Redisson...");

        validateStartedStatus();

        redisson.shutdown();
    }

    /**
     * 获取Redisson客户端是否初始化
     */
    @Override
    public boolean isInitialized() {
        return redisson != null;
    }

    /**
     * 获取Redisson客户端连接是否正常
     */
    @Override
    public boolean isStarted() {
        if (redisson == null) {
            throw new RedissonException("Redisson isn't initialized");
        }

        return !redisson.isShutdown() && !redisson.isShuttingDown();
    }

    /**
     * 检查Redisson是否是启动状态
     */
    @Override
    public void validateStartedStatus() {
        if (redisson == null) {
            throw new RedissonException("Redisson isn't initialized");
        }

        if (!isStarted()) {
            throw new RedissonException("Redisson is closed");
        }
    }

    /**
     * 检查Redisson是否是关闭状态
     */
    @Override
    public void validateClosedStatus() {
        if (redisson == null) {
            throw new RedissonException("Redisson isn't initialized");
        }

        if (isStarted()) {
            throw new RedissonException("Redisson is started");
        }
    }

    /**
     * 获取Redisson客户端
     */
    @Override
    public RedissonClient getRedisson() {
        return redisson;
    }
}