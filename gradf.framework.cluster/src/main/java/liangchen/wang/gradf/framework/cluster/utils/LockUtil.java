package liangchen.wang.gradf.framework.cluster.utils;


import liangchen.wang.gradf.framework.api.ILock;
import liangchen.wang.gradf.framework.cluster.enumeration.ClusterStatus;
import liangchen.wang.gradf.framework.data.utils.DbLockUtil;

/**
 * @author LiangChen.Wang
 */
public enum LockUtil {
    //
    INSTANCE;

    public ILock obtainLock() {
        if (ClusterStatus.INSTANCE.isRedisEnable()) {
            return RedisLockUtil.INSTANCE.obtainLock();
        }
        return DbLockUtil.INSTANCE.obtainLock();
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
