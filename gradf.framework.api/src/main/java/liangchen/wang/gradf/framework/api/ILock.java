package liangchen.wang.gradf.framework.api;

/**
 * @author LiangChen.Wang
 */
public interface ILock {

    boolean lock(String lockKey);

    void unlock(String lockKey);

    void executeInLock(String lockKey, VoidCallback callback);

    <R> R executeInLock(String lockKey, Callback<R> callback);

    @FunctionalInterface
    interface Callback<R> {
        R execute();
    }

    @FunctionalInterface
    interface VoidCallback {
        void execute();
    }
}
