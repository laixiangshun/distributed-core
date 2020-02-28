package com.htwz.configuration;

import com.htwz.aop.LockAutoScanProxy;
import com.htwz.aop.LockInterceptor;
import com.htwz.constant.LockConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁Aop配置
 *
 * @author lxs
 */
@Configuration
public class LockAopConfiguration {

    @Value("${" + LockConstant.LOCK_SCAN_PACKAGES + ":'com.htwz'}")
    private String scanPackages;

    @Bean
    public LockAutoScanProxy lockAutoScanProxy() {
        return new LockAutoScanProxy(scanPackages);
    }

    @Bean
    public LockInterceptor lockInterceptor() {
        return new LockInterceptor();
    }
}