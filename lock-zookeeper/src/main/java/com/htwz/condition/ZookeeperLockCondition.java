package com.htwz.condition;

import com.htwz.constant.LockConstant;




public class ZookeeperLockCondition extends AquariusCondition {

    public ZookeeperLockCondition() {
        super(LockConstant.LOCK_TYPE, LockConstant.LOCK_TYPE_ZOOKEEPER);
    }
}