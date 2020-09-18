package liangchen.wang.gradf.framework.data.api;

/**
 * @author LiangChen.Wang
 */
public interface ILock {

    boolean lock(String lockKey);

    void unlock(String lockKey);

    void executeInLock(String lockKey, VoidCallback callback);

    <R> R executeInLock(String lockKey, Callback<R> callback);

    interface Callback<R> {
        R execute();
    }

    interface VoidCallback {
        void execute();
    }
}
