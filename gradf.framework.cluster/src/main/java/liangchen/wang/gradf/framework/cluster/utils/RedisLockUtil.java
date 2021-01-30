package liangchen.wang.gradf.framework.cluster.utils;

import liangchen.wang.gradf.framework.api.ILock;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.springboot.context.BeanLoader;

/**
 * @author LiangChen.Wang
 */
public enum RedisLockUtil {
    //
    INSTANCE;

    public ILock obtainLock() {
        ILock lock = BeanLoader.getBean("Gradf_Distributed_RedisLock");
        Assert.INSTANCE.notNull(lock, "Redis未初始化");
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
