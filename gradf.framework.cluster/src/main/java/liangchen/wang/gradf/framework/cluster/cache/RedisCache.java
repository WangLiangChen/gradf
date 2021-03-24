package liangchen.wang.gradf.framework.cluster.cache;

import liangchen.wang.gradf.framework.cache.override.Cache;
import liangchen.wang.gradf.framework.commons.enumeration.Symbol;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 扩展补充spring的RedisCache
 *
 * @author LiangChen.Wang
 */
public class RedisCache extends org.springframework.data.redis.cache.RedisCache implements Cache {
    private final static Logger logger = LoggerFactory.getLogger(RedisCache.class);
    private final String name;
    private final long ttl;
    private final boolean allowNullValues;
    private final BoundSetOperations<Object, Object> keys;
    private final String loggerPrefix;

    public RedisCache(String name, long ttl, boolean allowNullValues, RedisTemplate<Object, Object> redisTemplate) {
        super(name, RedisCacheCreator.INSTANCE.cacheWriter(redisTemplate), RedisCacheCreator.INSTANCE.cacheConfig(ttl, allowNullValues));
        this.name = name;
        this.ttl = ttl;
        this.allowNullValues = allowNullValues;
        String keysKey = this.createCacheKey("keys");
        this.keys = redisTemplate.boundSetOps(keysKey);
        // 有key才能设置expire,所以先add
        if (keys.getExpire() < 0) {
            this.keys.add(Symbol.BLANK.getSymbol());
            this.keys.expire(ttl, TimeUnit.MILLISECONDS);
        }
        this.loggerPrefix = String.format("name:%s,ttl:%s,allowNullValues:%s", name, ttl, allowNullValues);
        logger.debug(loggerPrefix("Constructor"));
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader, long ttl) {
        return null;
    }

    @Override
    public synchronized <T> T get(Object key, Callable<T> valueLoader) {
        T t = super.get(key, valueLoader);
        if (null == t) {
            logger.debug(loggerPrefix() + ",get,key:{},missed", name, key);
            return null;
        }
        logger.debug(loggerPrefix() + ",get,key:{},hit the value:{}", name, key, t);
        return t;
    }

    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper valueWrapper = super.get(key);
        if (null == valueWrapper) {
            logger.debug(loggerPrefix() + ",get,key:{},missed", name, key);
            return null;
        }
        logger.debug(loggerPrefix() + ",get,key:{},hit the value:{}", name, key, valueWrapper.get());
        return valueWrapper;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        T t = super.get(key, type);
        if (null == t) {
            logger.debug(loggerPrefix() + ",get,key:{},missed", name, key);
            return null;
        }
        logger.debug(loggerPrefix() + ",get,key:{},hit the value:{}", name, key, t);
        return t;
    }

    @Override
    public void put(Object key, Object value, long ttl) {

    }

    @Override
    public void put(Object key, Object value) {
        super.put(key, value);
        keys.add(key);
        logger.debug(loggerPrefix() + ",put,key:{},value:{}", name, key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper valueWrapper = super.putIfAbsent(key, value);
        if (valueWrapper == null) {
            keys.add(key);
            logger.debug(loggerPrefix() + ",putIfAbsent,key:{},value:{},data is absent,put it", name, key, value);
        }
        logger.debug(loggerPrefix() + ",putIfAbsent key:{},value:{},data is present,abort it,existing:{} ", name, key, value, valueWrapper.get());
        return valueWrapper;
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value, long ttl) {
        return null;
    }


    @Override
    public void evict(Object key) {
        super.evict(key);
        keys.remove(key);
        logger.debug(loggerPrefix() + ",evict,key:{}", name, key);
    }

    @Override
    public boolean evictIfPresent(Object key) {
        boolean present = super.evictIfPresent(key);
        if (present) {
            keys.remove(key);
        }
        logger.debug(loggerPrefix() + ",evictIfPresent,key:{},present:{}", name, key, present);
        return present;
    }

    @Override
    public void clear() {
        super.clear();
        Set<Object> members = keys.members();
        keys.remove(members.toArray());
        logger.debug(loggerPrefix() + ",clear", name);
    }

    @Override
    public boolean invalidate() {
        boolean invalidate = super.invalidate();
        logger.debug(loggerPrefix() + ",invalidate:{}", invalidate);
        return invalidate;
    }

    @Override
    public Set<Object> keys() {
        return keys.members();
    }

    @Override
    public boolean containsKey(Object key) {
        Assert.INSTANCE.notNull(key, "key不能为null");
        return keys.isMember(key);
    }

    @Override
    public long getTtl() {
        return 0;
    }

    private String loggerPrefix() {
        return "GradfRedisCache,name:{}";
    }

    private String loggerPrefix(String suffix) {
        return String.format("%s,%s", loggerPrefix, suffix);
    }
}
