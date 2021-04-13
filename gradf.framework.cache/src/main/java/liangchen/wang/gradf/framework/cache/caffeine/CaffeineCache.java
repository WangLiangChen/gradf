package liangchen.wang.gradf.framework.cache.caffeine;

import liangchen.wang.gradf.framework.cache.override.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 */
public class CaffeineCache extends org.springframework.cache.caffeine.CaffeineCache implements Cache {
    private final static Logger logger = LoggerFactory.getLogger(CaffeineCache.class);
    private final String name;
    private final long ttl;
    private final boolean allowNullValues;
    private final Set<Object> keys;

    public CaffeineCache(String name, long ttl, boolean allowNullValues) {
        super(name, CaffeineCacheCreator.INSTANCE.nativeCaffeineCache(ttl, TimeUnit.MILLISECONDS), allowNullValues);
        this.name = name;
        this.ttl = ttl;
        this.allowNullValues = allowNullValues;
        this.keys = new CopyOnWriteArraySet<>();
        logger.debug("Construct {}", this.toString());
    }


    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        T value = super.get(key, valueLoader);
        // 此时为同步调用，返回后会设置缓存，所以这里需要添加key
        this.keys.add(key);
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        super.put(key, value);
        keys.add(key);
    }

    @Override
    public void evict(Object key) {
        super.evict(key);
        keys.remove(key);
    }

    @Override
    public void clear() {
        super.clear();
        keys.clear();
    }

    @Override
    public Set<Object> keys() {
        return this.keys;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.keys.contains(key);
    }

    @Override
    public long getTtl() {
        return this.ttl;
    }

    @Override
    public String toString() {
        return "CaffeineCache{" +
                "name='" + name + '\'' +
                ", ttl=" + ttl +
                ", allowNullValues=" + allowNullValues +
                '}';
    }
}
