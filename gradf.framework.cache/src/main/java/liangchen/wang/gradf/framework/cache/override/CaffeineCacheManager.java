package liangchen.wang.gradf.framework.cache.override;


import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiangChen.Wang 2021/4/15
 */
public class CaffeineCacheManager extends org.springframework.cache.caffeine.CaffeineCacheManager implements CacheManager {
    private boolean dynamic = true;
    @Nullable
    private CacheLoader<Object, Object> cacheLoader;
    private Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
    private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    @Override
    public Cache getCache(String cacheName) {
        return getCache(cacheName, 0L);
    }

    @Override
    public Cache getCache(String cacheName, long ttl) {
        return this.cacheMap.computeIfAbsent(cacheName, name -> this.dynamic ? createCaffeineCache(name, ttl) : null);
    }

    protected Cache createCaffeineCache(String name, long ttl) {
        return adaptCaffeineCache(name, createNativeCaffeineCache(ttl));
    }

    @Override
    protected Cache adaptCaffeineCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache) {
        return new liangchen.wang.gradf.framework.cache.caffeine.CaffeineCache(name, cache, isAllowNullValues());
    }

    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(long ttl) {
        if (ttl > 0) {
            this.cacheBuilder.expireAfterWrite(Duration.ofMillis(ttl));
        } else {
            this.cacheBuilder.expireAfterWrite(Duration.ZERO);
        }
        return (this.cacheLoader != null ? this.cacheBuilder.build(this.cacheLoader) : this.cacheBuilder.build());
    }

    @Override
    public void setCacheLoader(CacheLoader<Object, Object> cacheLoader) {
        super.setCacheLoader(cacheLoader);
        this.cacheLoader = cacheLoader;
    }

    @Override
    public void setCaffeine(Caffeine<Object, Object> caffeine) {
        super.setCaffeine(caffeine);
        this.cacheBuilder = caffeine;
    }

    @Override
    public void setCacheNames(@Nullable Collection<String> cacheNames) {
        if (cacheNames != null) {
            for (String name : cacheNames) {
                this.cacheMap.put(name, createCaffeineCache(name, 0L));
            }
            this.dynamic = false;
        } else {
            this.dynamic = true;
        }
    }

    @Override
    public Collection<String> getCacheNames() {
        Set<String> cacheNames = new LinkedHashSet<>(super.getCacheNames());
        cacheNames.addAll(this.cacheMap.keySet());
        return Collections.unmodifiableSet(cacheNames);
    }
}
