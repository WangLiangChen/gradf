package liangchen.wang.gradf.framework.cache.cluster.override;

import liangchen.wang.gradf.framework.cache.override.CacheManager;
import liangchen.wang.gradf.framework.commons.lock.LocalLockUtil;
import liangchen.wang.gradf.framework.commons.lock.LockReader;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author LiangChen.Wang 2021/4/16
 */
public class RedisCacheManager extends org.springframework.data.redis.cache.RedisCacheManager implements CacheManager {
    private final String LOCK_KEY = "RedisCacheManager";
    private final RedisTemplate<Object, Object> redisTemplate;
    private final RedisCacheWriter cacheWriter;
    private RedisCacheConfiguration defaultCacheConfig;
    private final boolean allowInFlightCacheCreation;
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    public RedisCacheManager(RedisTemplate<Object, Object> redisTemplate, RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig) {
        super(cacheWriter, defaultCacheConfig);
        this.redisTemplate = redisTemplate;
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfig;
        this.allowInFlightCacheCreation = true;
    }

    public RedisCacheManager(RedisTemplate<Object, Object> redisTemplate, RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfig, initialCacheNames);
        this.redisTemplate = redisTemplate;
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfig;
        this.allowInFlightCacheCreation = true;
    }

    public RedisCacheManager(RedisTemplate<Object, Object> redisTemplate, RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig, boolean allowInFlightCacheCreation, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfig, allowInFlightCacheCreation, initialCacheNames);
        this.redisTemplate = redisTemplate;
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfig;
        this.allowInFlightCacheCreation = allowInFlightCacheCreation;
    }

    public RedisCacheManager(RedisTemplate<Object, Object> redisTemplate, RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
        super(cacheWriter, defaultCacheConfig, initialCacheConfigurations);
        this.redisTemplate = redisTemplate;
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfig;
        this.allowInFlightCacheCreation = true;
    }

    public RedisCacheManager(RedisTemplate<Object, Object> redisTemplate, RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfig, initialCacheConfigurations, allowInFlightCacheCreation);
        this.redisTemplate = redisTemplate;
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfig;
        this.allowInFlightCacheCreation = allowInFlightCacheCreation;
    }

    @Override
    @Nullable
    public Cache getCache(String cacheName) {
        return getCache(cacheName, 0L);
    }

    @Override
    @Nullable
    public Cache getCache(String cacheName, long ttl) {
        String lockKey = String.format("%s::%s", LOCK_KEY, cacheName);
        return LocalLockUtil.INSTANCE.readWriteInReadWriteLock(lockKey, () -> {
            Cache cache = this.cacheMap.get(cacheName);
            if (null == cache) {
                return null;
            }
            return new LockReader.LockValueWrapper<>(cache);
        }, () -> {
            Cache missingCache = getMissingCache(cacheName, ttl);
            if (null == missingCache) {
                return null;
            }
            missingCache = decorateCache(missingCache);
            this.cacheMap.put(cacheName, missingCache);
            return missingCache;
        });
    }

    protected RedisCache getMissingCache(String cacheName, long ttl) {
        RedisCacheConfiguration cacheConfig = defaultCacheConfig;
        if (ttl > 0) {
            cacheConfig = defaultCacheConfig.entryTtl(Duration.ofMillis(ttl));
        }
        return allowInFlightCacheCreation ? createRedisCache(cacheName, cacheConfig) : null;
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        return new liangchen.wang.gradf.framework.cache.cluster.redis.RedisCache(name, cacheWriter, cacheConfig, redisTemplate);
    }

    @Override
    public Collection<String> getCacheNames() {
        Set<String> cacheNames = new LinkedHashSet<>(super.getCacheNames());
        cacheNames.addAll(this.cacheMap.keySet());
        return Collections.unmodifiableSet(cacheNames);
    }
}
