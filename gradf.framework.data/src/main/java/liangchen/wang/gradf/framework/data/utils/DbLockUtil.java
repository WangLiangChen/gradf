package liangchen.wang.gradf.framework.data.utils;

import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.api.ILock;
import liangchen.wang.gradf.framework.springboot.context.BeanLoader;

/**
 * @author LiangChen.Wang
 */
public enum DbLockUtil {
    //
    INSTANCE;

    public ILock obtainLock() {
        ILock lock = BeanLoader.getBean("Crdf_Data_ReplaceIntoLock");
        Assert.INSTANCE.notNull(lock, "DbLock未初始化");
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
