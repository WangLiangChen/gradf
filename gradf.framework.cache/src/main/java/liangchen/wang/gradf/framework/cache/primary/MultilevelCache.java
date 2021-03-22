package liangchen.wang.gradf.framework.cache.primary;

import liangchen.wang.gradf.framework.cache.override.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.Set;
import java.util.concurrent.Callable;


/**
 * @author LiangChen.Wang 2021/3/22
 */
public class MultilevelCache extends AbstractValueAdaptingCache implements Cache {

    /**
     * Create an {@code AbstractValueAdaptingCache} with the given setting.
     *
     * @param allowNullValues whether to allow for {@code null} values
     */
    protected MultilevelCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    @Override
    public ValueWrapper get(Object key, long ttl) {
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type, long ttl) {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader, long ttl) {
        return null;
    }

    @Override
    public void put(Object key, Object value, long ttl) {

    }

    @Override
    public Set<Object> keys() {
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public long getTtl() {
        return 0;
    }

    @Override
    protected Object lookup(Object key) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {

    }
}
