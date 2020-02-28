package com.htwz.configuration;
import com.htwz.LockDelegate;
import com.htwz.LockExecutor;
import com.htwz.condition.LocalLockCondition;
import com.htwz.impl.LocalLockDelegateImpl;
import com.htwz.impl.LocalLockExecutorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.locks.Lock;

@Configuration
public class LocalLockConfiguration {

    @Bean
    @Conditional({LocalLockCondition.class})
    public LockDelegate localLockDelegate() {
        return new LocalLockDelegateImpl();
    }

    @Bean
    @Conditional(LocalLockCondition.class)
    public LockExecutor<Lock> localLockExecutor() {
        return new LocalLockExecutorImpl();
    }
}