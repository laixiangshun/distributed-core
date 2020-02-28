package com.htwz.aop;

import com.htwz.annotation.Lock;
import com.htwz.annotation.ReadLock;
import com.htwz.annotation.WriteLock;
import com.htwz.proxy.aop.DefaultAutoScanProxy;
import com.htwz.proxy.mode.ProxyMode;
import com.htwz.proxy.mode.ScanMode;

import java.lang.annotation.Annotation;

/**
 * 分布式锁自动扫描代理
 *
 * @author lxs
 */
public class LockAutoScanProxy extends DefaultAutoScanProxy {

    private static final long serialVersionUID = -957037966342626931L;

    private String[] commonInterceptorNames;

    @SuppressWarnings("rawtypes")
    private Class[] methodAnnotations;

    public LockAutoScanProxy(String scanPackages) {
        super(scanPackages, ProxyMode.BY_METHOD_ANNOTATION_ONLY, ScanMode.FOR_METHOD_ANNOTATION_ONLY);
    }

    /**
     * 返回具有调用拦截的全局切面实现类的Bean名，拦截类必须实现MethodInterceptor接口, 可以多个
     * 如果返回null， 全局切面代理关闭
     */
    @Override
    protected String[] getCommonInterceptorNames() {
        if (commonInterceptorNames == null) {
            commonInterceptorNames = new String[]{"lockInterceptor"};
        }

        return commonInterceptorNames;
    }

    /**
     * 返回接口或者类的方法名上的注解，可以多个，如果接口或者类中方法名上存在一个或者多个该列表中的注解，即认为该接口或者类需要被代理和扫描
     * 如果返回null，则对列表中的注解不做代理和扫描
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Class<? extends Annotation>[] getMethodAnnotations() {
        if (methodAnnotations == null) {
            methodAnnotations = new Class[]{Lock.class, ReadLock.class, WriteLock.class};
        }

        return methodAnnotations;
    }
}