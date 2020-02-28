package com.htwz.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AquariusConfiguration.class, LockAopConfiguration.class, RedisLockConfiguration.class, ZookeeperLockConfiguration.class, LocalLockConfiguration.class})
public class LockConfiguration {

}