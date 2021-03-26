package liangchen.wang.gradf.framework.commons.lock;

/**
 * @param <R>
 * @author LiangChen.Wang
 */
@FunctionalInterface
public interface LockReader<R> {
    LockValueWrapper<R> read() throws Exception;
}

class LockValueWrapper<R> {
    private final R object;

    LockValueWrapper(R object) {
        this.object = object;
    }

    public R get() {
        return object;
    }

    public static <R> LockValueWrapper<R> of(R object) {
        return new LockValueWrapper<>(object);
    }
}
