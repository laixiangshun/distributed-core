package com.htwz.condition;



import com.htwz.constant.LockConstant;

public class RedisLockCondition extends AquariusCondition {
    public RedisLockCondition() {
        super(LockConstant.LOCK_TYPE, LockConstant.LOCK_TYPE_REDIS);
    }
}