package liangchen.wang.gradf.framework.commons.lock;

/**
 * @param <R>
 * @author LiangChen.Wang
 */
@FunctionalInterface
public interface LockReader<R> {
    R read() throws Exception;
}
