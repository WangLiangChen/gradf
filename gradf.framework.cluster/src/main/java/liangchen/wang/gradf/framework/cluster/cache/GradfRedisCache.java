package liangchen.wang.gradf.framework.cluster.cache;

import liangchen.wang.gradf.framework.cache.primary.GradfCache;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCache;
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
public class GradfRedisCache extends RedisCache implements GradfCache {
    private final static Logger logger = LoggerFactory.getLogger(GradfRedisCache.class);
    private final String name;
    private final BoundSetOperations<Object, Object> boundSetOperations;

    public GradfRedisCache(String name, long ttl, TimeUnit timeUnit, RedisTemplate<Object, Object> redisTemplate) {
        super(name, RedisCacheCreator.INSTANCE.cacheWriter(redisTemplate), RedisCacheCreator.INSTANCE.cacheConfig(ttl, timeUnit));
        this.name = name;
        String keys = this.createCacheKey("keys");
        this.boundSetOperations = redisTemplate.boundSetOps(keys);
        // 设置expire 有key才管用 所以先add
        if (boundSetOperations.getExpire() < 0) {
            this.boundSetOperations.add("");
            this.boundSetOperations.expire(ttl, timeUnit);
        }
        logger.debug(loggerPrefix() + "is created,ttl:{}ms", name, timeUnit.toMillis(ttl));
    }

    @Override
    public Set<Object> keys() {
        return boundSetOperations.members();
    }

    @Override
    public boolean containsKey(Object key) {
        Assert.INSTANCE.notNull(key, "key不能为null");
        return boundSetOperations.isMember(key);
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
    public void put(Object key, Object value) {
        super.put(key, value);
        boundSetOperations.add(key);
        logger.debug(loggerPrefix() + ",put,key:{},value:{}", name, key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        ValueWrapper valueWrapper = super.putIfAbsent(key, value);
        if (valueWrapper == null) {
            boundSetOperations.add(key);
            logger.debug(loggerPrefix() + ",putIfAbsent,key:{},value:{},data is absent,put it", name, key, value);
        }
        logger.debug(loggerPrefix() + ",putIfAbsent key:{},value:{},data is present,abort it,existing:{} ", name, key, value, valueWrapper.get());
        return valueWrapper;
    }

    @Override
    public void evict(Object key) {
        super.evict(key);
        boundSetOperations.remove(key);
        logger.debug(loggerPrefix() + ",evict,key:{}", name, key);
    }

    @Override
    public boolean evictIfPresent(Object key) {
        boolean present = super.evictIfPresent(key);
        if (present) {
            boundSetOperations.remove(key);
        }
        logger.debug(loggerPrefix() + ",evictIfPresent,key:{},present:{}", name, key, present);
        return present;
    }

    @Override
    public void clear() {
        super.clear();
        Set<Object> members = boundSetOperations.members();
        boundSetOperations.remove(members.toArray());
        logger.debug(loggerPrefix() + ",clear", name);
    }

    @Override
    public boolean invalidate() {
        boolean invalidate = super.invalidate();
        logger.debug(loggerPrefix() + ",invalidate:{}", invalidate);
        return invalidate;
    }

    private String loggerPrefix() {
        return "GradfRedisCache,name:{}";
    }
}
