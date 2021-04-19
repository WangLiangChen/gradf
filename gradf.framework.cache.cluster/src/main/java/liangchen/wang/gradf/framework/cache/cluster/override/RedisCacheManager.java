package liangchen.wang.gradf.framework.cache.cluster.override;

import liangchen.wang.gradf.framework.cache.cluster.redis.RedisCache;
import liangchen.wang.gradf.framework.cache.override.CacheManager;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author LiangChen.Wang 2021/4/16
 */
public class RedisCacheManager extends org.springframework.data.redis.cache.RedisCacheManager implements CacheManager {
    private final RedisCacheConfiguration defaultCacheConfiguration;
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    public RedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    public RedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheNames);
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    public RedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, boolean allowInFlightCacheCreation, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation, initialCacheNames);
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    public RedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations);
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    public RedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
        this.defaultCacheConfiguration = defaultCacheConfiguration;
    }

    @Override
    public Cache getCache(String cacheName, long ttl) {
        // Quick check for existing cache...
        Cache cache = this.cacheMap.get(cacheName);
        if (cache != null) {
            return cache;
        }

        // The provider may support on-demand cache creation...
        Cache missingCache = getMissingCache(cacheName);
        if (missingCache != null) {
            // Fully synchronize now for missing cache registration
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(cacheName);
                if (cache == null) {
                    cache = decorateCache(missingCache);
                    this.cacheMap.put(cacheName, cache);
                }
            }
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        Collection<String> cacheNames = new HashSet<>(super.getCacheNames());
        cacheNames.addAll(this.cacheMap.keySet());
        return Collections.unmodifiableCollection(cacheNames);
    }

    private Cache getMissingCache(String cacheName, long ttl) {
        boolean allowNullValues=defaultCacheConfiguration.getAllowCacheNullValues();
        return new RedisCache(cacheName,allowNullValues);
    }
}
