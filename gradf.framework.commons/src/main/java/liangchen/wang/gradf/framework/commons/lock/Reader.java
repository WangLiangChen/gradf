package liangchen.wang.gradf.framework.commons.lock;

/**
 * @param <R>
 * @author LiangChen.Wang
 */
@FunctionalInterface
public interface Reader<R> {
    R read() throws Exception;
}
