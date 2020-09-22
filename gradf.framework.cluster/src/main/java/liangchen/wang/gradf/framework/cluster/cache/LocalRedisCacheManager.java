package liangchen.wang.gradf.framework.cluster.cache;


import liangchen.wang.crdf.framework.cache.primary.CacheNameResolver;
import liangchen.wang.crdf.framework.commons.encryption.HashUtil;
import liangchen.wang.crdf.framework.commons.exeception.ErrorException;
import liangchen.wang.crdf.framework.commons.json.JSONUtil;
import liangchen.wang.crdf.framework.commons.utils.LocalLockUtil;
import liangchen.wang.crdf.framework.commons.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 */
//实现CacheManagerCustomizer接口，可以定制CacheManager参数
public class LocalRedisCacheManager implements CacheManager {
    private static final Logger logger = LoggerFactory.getLogger(CaffeineRedisCacheManager.class);
    private final Map<String, Cache> cacheMap = new HashMap<>();
    private boolean transactionAware = false;

    private final StringRedisTemplate stringRedisTemplate;

    public CaffeineRedisCacheManager() {
        this(null);
    }

    @SuppressWarnings("unchecked")
    public CaffeineRedisCacheManager(StringRedisTemplate stringRedisTemplate) {
        super();
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public Cache getCache(String name, TimeUnit timeUnit, long ttl) {
        String md5Name = md5Name(name);
        Cache cache = this.cacheMap.get(md5Name);
        if (cache != null) {
            return cache;
        }
        try {
            return LocalLockUtil.INSTANCE.readWriteInReadWriteLock(md5Name, () -> this.cacheMap.get(md5Name), () -> {
                Cache newCache = new CaffeineRedisCache(md5Name, name, timeUnit, ttl, true, stringRedisTemplate);
                newCache = decorateCache(newCache);
                this.cacheMap.put(md5Name, newCache);
                return newCache;
            });
        } catch (Throwable t) {
            throw new ErrorException(t);
        }
    }

    @Override
    public Cache getCache(String name) {
        CacheNameResolver cacheNameResolver = new CacheNameResolver(name);
        return getCache(cacheNameResolver.getName(), cacheNameResolver.getTimeUnit(), cacheNameResolver.getTtl());
    }

    @Override
    public Optional<Cache> getCacheIfPresent(String name) {
        CacheNameResolver cacheNameResolver = new CacheNameResolver(name);
        String md5Name = md5Name(cacheNameResolver.getName());
        return Optional.ofNullable(this.cacheMap.get(md5Name));
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

    private String md5Name(String name) {
        if (name.length() > 32) {
            name = HashUtil.INSTANCE.md5Digest(name);
        }
        return name;
    }

    public void receiveRedisMessage(String json) {
        logger.debug("接收到CacheMessage消息：{}", json);
        if (StringUtil.INSTANCE.isBlank(json)) {
            return;
        }
        CacheMessage message = JSONUtil.INSTANCE.parseObject(json, CacheMessage.class);
        String name = message.getName();
        if (StringUtil.INSTANCE.isBlank(name)) {
            logger.error("缓存名称为空");
            return;
        }
        CaffeineRedisCache cache = (CaffeineRedisCache) this.getCache(name);
        CacheMessage.CacheAction action = message.getAction();
        switch (action) {
            case CacheMessage.CacheAction.clear:
                cache.clearLocal();
                break;
            case CacheMessage.CacheAction.evict:
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
