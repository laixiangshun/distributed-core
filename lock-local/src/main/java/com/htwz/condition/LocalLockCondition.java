package com.htwz.condition;

import com.htwz.constant.LockConstant;

/**
 * 本地锁condition
 * @author lxs
 */
public class LocalLockCondition extends AquariusCondition {

    public LocalLockCondition() {
        super(LockConstant.LOCK_TYPE, LockConstant.LOCK_TYPE_LOCAL);
    }
}