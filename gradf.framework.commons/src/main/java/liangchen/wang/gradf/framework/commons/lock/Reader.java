package liangchen.wang.gradf.framework.commons.lock;

@FunctionalInterface
public interface Reader<R> {
    R read() throws Throwable;
}
