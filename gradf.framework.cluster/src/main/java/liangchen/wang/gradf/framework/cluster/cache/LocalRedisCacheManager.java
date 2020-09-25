package liangchen.wang.gradf.framework.cluster.cache;

import liangchen.wang.gradf.framework.cache.primary.CacheNameResolver;
import liangchen.wang.gradf.framework.cache.primary.GradfCache;
import liangchen.wang.gradf.framework.cache.primary.GradfCacheManager;
import liangchen.wang.gradf.framework.cluster.cache.CacheMessage.CacheAction;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.lock.LocalLockUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 * 实现CacheManagerCustomizer接口，可以定制CacheManager参数
 */
public class LocalRedisCacheManager implements GradfCacheManager {
    private static final Logger logger = LoggerFactory.getLogger(LocalRedisCacheManager.class);
    private final Map<String, GradfCache> cacheMap = new HashMap<>();
    private boolean transactionAware = false;

    private final RedisTemplate<Object, Object> redisTemplate;

    public LocalRedisCacheManager() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public LocalRedisCacheManager(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GradfCache getCache(String name, long ttl, TimeUnit timeUnit) {
        try {
            return LocalLockUtil.INSTANCE.readWriteInReadWriteLock(name, () -> this.cacheMap.get(name), () -> {
                Cache newCache = new LocalRedisCache(name, true, ttl, timeUnit, redisTemplate);
                newCache = decorateCache(newCache);
                GradfCache gradfCache = GradfCache.class.cast(newCache);
                this.cacheMap.put(name, gradfCache);
                return gradfCache;
            });
        } catch (Throwable t) {
            throw new ErrorException(t);
        }
    }

    @Override
    public Cache getCache(String name) {
        CacheNameResolver cacheNameResolver = new CacheNameResolver(name);
        return getCache(cacheNameResolver.getName(), cacheNameResolver.getTtl(), cacheNameResolver.getTimeUnit());
    }

    @Override
    public Optional<GradfCache> getCacheIfPresent(String name) {
        CacheNameResolver cacheNameResolver = new CacheNameResolver(name);
        return Optional.ofNullable(this.cacheMap.get(cacheNameResolver.getName()));
    }

    @Override
    public void clear() {
        this.cacheMap.forEach((k, v) -> v.clear());
        this.cacheMap.clear();
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }

    private Cache decorateCache(Cache cache) {
        return (this.transactionAware ? new TransactionAwareCacheDecorator(cache) : cache);
    }

    public void receiveRedisMessage(String json) {
        logger.debug("接收到CacheMessage消息：{}", json);
        if (StringUtil.INSTANCE.isBlank(json)) {
            return;
        }
        CacheMessage message = JsonUtil.INSTANCE.parseObject(json, CacheMessage.class);
        String name = message.getName();
        if (StringUtil.INSTANCE.isBlank(name)) {
            logger.error("缓存名称为空");
            return;
        }
        LocalRedisCache cache = (LocalRedisCache) this.getCache(name);
        CacheAction action = message.getAction();
        switch (action) {
            case clear:
                cache.clearLocal();
                break;
            case evict:
                Object key = message.getKey();
                if (null == key) {
                    logger.error("缓存Key为空");
                    break;
                }
                cache.evictLocal(key);
                break;
            default:
                break;
        }
    }
}
