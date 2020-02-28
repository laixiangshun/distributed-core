package com.htwz.annotation;

/**
 * Springboot 启动分布式锁的注解
 *
 * @author lxs
 * @version 1.0
 */

import com.htwz.aop.LockImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(LockImportSelector.class)
public @interface EnableLock {

}