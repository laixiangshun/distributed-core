package com.htwz.aop;

import com.htwz.annotation.EnableLock;
import com.htwz.constant.LockConstant;
import com.htwz.selector.AbstractImportSelector;
import com.htwz.selector.RelaxedPropertyResolver;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.LOWEST_PRECEDENCE - 100)
public class LockImportSelector extends AbstractImportSelector<EnableLock> {

    @Override
    protected boolean isEnabled() {
        return new RelaxedPropertyResolver(getEnvironment()).getProperty(LockConstant.LOCK_ENABLED, Boolean.class, Boolean.TRUE);
    }
}