package liangchen.wang.gradf.framework.cluster.utils;

import liangchen.wang.crdf.framework.commons.validator.Assert;
import liangchen.wang.crdf.framework.commons.validator.AssertLevel;
import liangchen.wang.crdf.framework.springboot.api.ILock;
import liangchen.wang.crdf.framework.springboot.context.BeanLoader;

public enum RedisLockUtil {
    INSTANCE;

    public ILock obtainLock() {
        ILock lock = BeanLoader.getBean("Crdf_Distributed_RedisLock");
        Assert.INSTANCE.notNull(lock, AssertLevel.INFO, "Redis未初始化");
        return lock;
    }

    public void executeInLock(String lockKey, ILock.VoidCallback callback) {
        ILock lock = obtainLock();
        lock.executeInLock(lockKey, callback);
    }

    public <R> R executeInLock(String lockKey, ILock.Callback<R> callback) {
        ILock lock = obtainLock();
        return lock.executeInLock(lockKey, callback);
    }
}
