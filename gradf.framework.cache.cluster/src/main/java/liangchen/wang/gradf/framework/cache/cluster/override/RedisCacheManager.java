package liangchen.wang.gradf.framework.cache.cluster.override;

import liangchen.wang.gradf.framework.cache.override.CacheManager;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author LiangChen.Wang 2021/4/16
 */
public class RedisCacheManager extends org.springframework.data.redis.cache.RedisCacheManager implements CacheManager {
    private final RedisTemplate<Object, Object> redisTemplate;
    private final RedisCacheWriter cacheWriter;
    private RedisCacheConfiguration defaultCacheConfig;
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    public RedisCacheManager(RedisTemplate<Object, Object> redisTemplate, RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig) {
        super(cacheWriter, defaultCacheConfig);
        this.redisTemplate = redisTemplate;
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfig;
    }

    public RedisCacheManager(RedisTemplate<Object, Object> redisTemplate, RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfig, initialCacheNames);
        this.redisTemplate = redisTemplate;
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfig;
    }

    public RedisCacheManager(RedisTemplate<Object, Object> redisTemplate, RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig, boolean allowInFlightCacheCreation, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfig, allowInFlightCacheCreation, initialCacheNames);
        this.redisTemplate = redisTemplate;
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfig;
    }

    public RedisCacheManager(RedisTemplate<Object, Object> redisTemplate, RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
        super(cacheWriter, defaultCacheConfig, initialCacheConfigurations);
        this.redisTemplate = redisTemplate;
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfig;
    }

    public RedisCacheManager(RedisTemplate<Object, Object> redisTemplate, RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfig, initialCacheConfigurations, allowInFlightCacheCreation);
        this.redisTemplate = redisTemplate;
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfig;
    }

    @Override
    public Cache getCache(String cacheName, long ttl) {
        if (ttl > 0) {
            this.defaultCacheConfig = this.defaultCacheConfig.entryTtl(Duration.ofMillis(ttl));
        }
        return getCache(cacheName);
    }

    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
        return new liangchen.wang.gradf.framework.cache.cluster.redis.RedisCache(name, cacheWriter, cacheConfig, redisTemplate);
    }
}
