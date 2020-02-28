package com.htwz;

import com.htwz.entity.LockType;
import org.aopalliance.intercept.MethodInvocation;

public interface LockDelegate {

    Object invoke(MethodInvocation invocation, LockType lockType, String key, long leaseTime, long waitTime, boolean async, boolean fair) throws Throwable;
}