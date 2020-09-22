package liangchen.wang.gradf.framework.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import liangchen.wang.gradf.framework.cache.primary.GradfCache;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 */
public class GradfCaffeineCache extends AbstractValueAdaptingCache implements GradfCache {
    private final static Logger logger = LoggerFactory.getLogger(GradfCaffeineCache.class);
    private final ConcurrentMap<String, String> keys;
    private final CaffeineCache caffeineCache;

    public GradfCaffeineCache(String name, boolean allowNullValues, long ttl, TimeUnit timeUnit) {
        super(allowNullValues);
        keys = new ConcurrentHashMap<>();
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if (ttl > 0) {
            cacheBuilder.expireAfterWrite(ttl, timeUnit);
        }
        caffeineCache = new CaffeineCache(name, cacheBuilder.build());
    }

    @Override
    public void putKey(String key, String originalKey) {
        Assert.INSTANCE.notBlank(key, "key不能为空");
        Assert.INSTANCE.notBlank(originalKey, "originalKey不能为空");
        logger.debug("CaffeineCache,putKey,Key:{},originalKey:{}", key, originalKey);
        keys.put(key, originalKey);
    }

    @Override
    public void evictKey(String key) {
        Assert.INSTANCE.notBlank(key, "key不能为空");
        logger.debug("CaffeineCache,evictKey,Key:{}", key);
        keys.remove(key);
    }

    @Override
    public Set<String> keys() {
        return keys.keySet();
    }

    @Override
    public boolean containsKey(String key) {
        Assert.INSTANCE.notBlank(key, "key不能为空");
        return keys.containsKey(key);
    }


    @Override
    public String getName() {
        return caffeineCache.getName();
    }

    @Override
    public Object getNativeCache() {
        return caffeineCache.getNativeCache();
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        return caffeineCache.get(key, callable);
    }

    @Override
    public void put(Object key, Object value) {
        caffeineCache.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return caffeineCache.putIfAbsent(key, value);
    }

    @Override
    public void evict(Object key) {
        caffeineCache.evict(key);
    }

    @Override
    public boolean evictIfPresent(Object key) {
        return caffeineCache.evictIfPresent(key);
    }

    @Override
    public void clear() {
        caffeineCache.clear();
    }

    @Override
    public boolean invalidate() {
        return caffeineCache.invalidate();
    }

    @Override
    protected Object lookup(Object key) {
        Cache<Object, Object> nativeCache = (Cache<Object, Object>) getNativeCache();
        return nativeCache.getIfPresent(key);
    }
}
