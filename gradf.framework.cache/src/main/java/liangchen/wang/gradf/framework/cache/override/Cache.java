package liangchen.wang.gradf.framework.cache.override;

import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author LiangChen.Wang 2021/3/22
 */
public interface Cache extends org.springframework.cache.Cache {

    void put(Object key, Object value, long ttl);

    default ValueWrapper putIfAbsent(Object key, Object value, long ttl) {
        ValueWrapper existingValue = get(key);
        if (existingValue == null) {
            put(key, value, ttl);
        }
        return existingValue;
    }

    <T> T get(Object key, Callable<T> valueLoader, long ttl);

    Set<Object> keys();

    boolean containsKey(Object key);

    long getTtl();
}
