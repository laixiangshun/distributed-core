package com.htwz.configuration;


import com.htwz.LockDelegate;
import com.htwz.LockExecutor;
import com.htwz.condition.ZookeeperLockCondition;
import com.htwz.handler.CuratorHandler;
import com.htwz.handler.CuratorHandlerImpl;
import com.htwz.impl.ZookeeperLockDelegateImpl;
import com.htwz.impl.ZookeeperLockExecutorImpl;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZookeeperLockConfiguration {

    @Bean
    @Conditional(ZookeeperLockCondition.class)
    public LockDelegate zookeeperLockDelegate() {
        return new ZookeeperLockDelegateImpl();
    }

    @Bean
    @Conditional(ZookeeperLockCondition.class)
    public LockExecutor<InterProcessMutex> zookeeperLockExecutor() {
        return new ZookeeperLockExecutorImpl();
    }

    @Bean
    @Conditional(ZookeeperLockCondition.class)
    @ConditionalOnMissingBean
    public CuratorHandler curatorHandler() {
        return new CuratorHandlerImpl();
    }
}