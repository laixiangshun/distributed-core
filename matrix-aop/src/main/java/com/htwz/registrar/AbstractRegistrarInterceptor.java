package com.htwz.registrar;

import com.htwz.proxy.aop.AbstractInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.MutablePropertyValues;

public abstract class AbstractRegistrarInterceptor extends AbstractInterceptor {
    protected MutablePropertyValues annotationValues;

    public AbstractRegistrarInterceptor(MutablePropertyValues annotationValues) {
        this.annotationValues = annotationValues;
    }

    public MutablePropertyValues getAnnotationValues() {
        return annotationValues;
    }

    public String getInterface(MethodInvocation invocation) {
        return getMethod(invocation).getDeclaringClass().getCanonicalName();
    }
}