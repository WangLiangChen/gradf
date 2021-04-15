package liangchen.wang.gradf.framework.cache.override;

import liangchen.wang.gradf.framework.cache.caffeine.CaffeineCache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiangChen.Wang 2021/4/15
 */
public class CaffeineCacheManager extends org.springframework.cache.caffeine.CaffeineCacheManager implements CacheManager {

    private final Map<String, org.springframework.cache.Cache> cacheMap = new ConcurrentHashMap<>(16);

    @Override
    public org.springframework.cache.Cache getCache(String name, long ttl) {
        return this.cacheMap.computeIfAbsent(name, cacheName -> new CaffeineCache(name, ttl, super.isAllowNullValues()));
    }

    @Override
    public Collection<String> getCacheNames() {
        Collection<String> cacheNames = new HashSet<>(super.getCacheNames());
        cacheNames.addAll(this.cacheMap.keySet());
        return Collections.unmodifiableCollection(cacheNames);
    }
}
