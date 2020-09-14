package liangchen.wang.gradf.framework.commons.lock;

@FunctionalInterface
public interface Writer<R> {
    R write() throws Throwable;
}
