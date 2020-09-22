package liangchen.wang.gradf.framework.cache.function;

/**
 * @param <R>
 * @param <T>
 * @author LiangChen.Wang
 */
@FunctionalInterface
public interface Supplier<R, T extends Throwable> {
    R get() throws T;
}