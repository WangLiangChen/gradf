package liangchen.wang.gradf.framework.cache.override;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import liangchen.wang.gradf.framework.cache.caffeine.CaffeineCache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2021/4/15
 */
public class CaffeineCacheManager extends org.springframework.cache.caffeine.CaffeineCacheManager implements CacheManager {
    private final Map<String, org.springframework.cache.Cache> cacheMap = new ConcurrentHashMap<>(16);

    @Override
    public org.springframework.cache.Cache getCache(String name, long ttl) {
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if (ttl > 0) {
            cacheBuilder.expireAfterWrite(ttl, TimeUnit.MILLISECONDS);
        }
        setCaffeine(cacheBuilder);
        return this.cacheMap.computeIfAbsent(name, cacheName -> createCaffeineCache(name));
    }

    @Override
    public Collection<String> getCacheNames() {
        Collection<String> cacheNames = new HashSet<>(super.getCacheNames());
        cacheNames.addAll(this.cacheMap.keySet());
        return Collections.unmodifiableCollection(cacheNames);
    }

    @Override
    protected org.springframework.cache.Cache adaptCaffeineCache(String name, Cache<Object, Object> cache) {
        return new CaffeineCache(name, cache, super.isAllowNullValues());
    }
}
