package liangchen.wang.gradf.framework.commons.lock;

/**
 * @param <R>
 * @author LiangChen.Wang
 */
@FunctionalInterface
public interface Writer<R> {
    R write() throws Exception;
}
