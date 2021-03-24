package liangchen.wang.gradf.framework.cluster.cache;

import liangchen.wang.gradf.framework.cache.override.Cache;
import liangchen.wang.gradf.framework.commons.enumeration.Symbol;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        this.loggerPrefix = String.format("Cache(name:%s,ttl:%s,allowNullValues:%s)", name, ttl, allowNullValues);
        logger.debug(loggerPrefix("Constructor"));
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader, long ttl) {
        logger.debug(loggerPrefix("get", "key", "valueLoader", "ttl"), key, valueLoader, ttl);
        return super.get(key, valueLoader);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return this.get(key, valueLoader, 0L);
    }

    @Override
    public ValueWrapper get(Object key) {
        logger.debug(loggerPrefix("get", "key"), key);
        return super.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        logger.debug(loggerPrefix("get", "key", "type"), key, type);
        return super.get(key, type);
    }


    @Override
    public void put(Object key, Object value, long ttl) {
        logger.debug(loggerPrefix("put", "key", "value", "ttl"), key, value, ttl);
        super.put(key, value);
        keys.add(key);
    }

    @Override
    public void put(Object key, Object value) {
        this.put(key, value, 0L);
    }

    @Override
    public void evict(Object key) {
        logger.debug(loggerPrefix("evict", "key"), key);
        super.evict(key);
        keys.remove(key);
    }

    @Override
    public void clear() {
        logger.debug(loggerPrefix("clear"));
        super.clear();
        Set<Object> members = keys.members();
        keys.remove(members.toArray());
    }

    @Override
    public Set<Object> keys() {
        return this.keys.members();
    }

    @Override
    public boolean containsKey(Object key) {
        return keys.isMember(key);
    }

    @Override
    public long getTtl() {
        return this.ttl;
    }

    @Override
    public String getName() {
        return this.name;
    }

    private String loggerPrefix(String method, String... args) {
        String suffix = Arrays.stream(args).map(e -> String.format("%s:{}", e)).collect(Collectors.joining(","));
        if (null == suffix || suffix.length() == 0) {
            return String.format("%s\r\n - Method(name:%s)", loggerPrefix, method);
        }
        return String.format("%s\r\n - Method(name:%s,%s)", loggerPrefix, method, suffix);
    }
}
