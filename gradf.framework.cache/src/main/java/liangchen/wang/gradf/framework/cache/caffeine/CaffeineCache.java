package liangchen.wang.gradf.framework.cache.caffeine;

import liangchen.wang.gradf.framework.cache.override.Cache;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 */
public class CaffeineCache implements Cache {
    private final static Logger logger = LoggerFactory.getLogger(CaffeineCache.class);
    private final org.springframework.cache.caffeine.CaffeineCache springCaffeineCache;
    private final String name;
    private final long ttl;
    private final Set<Object> keys;

    public CaffeineCache(String name, long ttl, boolean allowNullValues) {
        springCaffeineCache = new org.springframework.cache.caffeine.CaffeineCache(name, CaffeineCacheCreator.INSTANCE.nativeCaffeineCache(ttl, TimeUnit.MILLISECONDS), allowNullValues);
        this.name = name;
        this.ttl = ttl;
        this.keys = new ConcurrentSkipListSet<>();
    }


    @Override
    public <T> T get(Object key, Callable<T> valueLoader, long ttl) {
        return springCaffeineCache.get(key, valueLoader);
    }

    @Override
    public ValueWrapper get(Object key) {
        return springCaffeineCache.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return springCaffeineCache.get(key, type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return springCaffeineCache.get(key, valueLoader);
    }

    @Override
    public void put(Object key, Object value, long ttl) {
        springCaffeineCache.put(key, value);
    }

    @Override
    public void put(Object key, Object value) {
        springCaffeineCache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        springCaffeineCache.evict(key);
    }

    @Override
    public void clear() {
        springCaffeineCache.clear();
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
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.springCaffeineCache;
    }


}
