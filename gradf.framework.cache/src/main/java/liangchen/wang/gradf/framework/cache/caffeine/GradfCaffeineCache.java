package liangchen.wang.gradf.framework.cache.caffeine;

import liangchen.wang.gradf.framework.cache.primary.GradfCache;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 */
public class GradfCaffeineCache extends org.springframework.cache.caffeine.CaffeineCache implements GradfCache {
    private final static Logger logger = LoggerFactory.getLogger(GradfCaffeineCache.class);
    private final String name;
    private final Set<Object> keys;

    public GradfCaffeineCache(String name, boolean allowNullValues, long ttl, TimeUnit timeUnit) {
        super(name, CaffeineCacheCreator.INSTANCE.nativeCaffeineCache(ttl, timeUnit), allowNullValues);
        this.name = name;
        this.keys = new ConcurrentSkipListSet<>();
        logger.debug(loggerPrefix() + "is created,ttl:{}ms", name, timeUnit.toMillis(ttl));
    }

    @Override
    public Set<Object> keys() {
        return keys;
    }

    @Override
    public boolean containsKey(Object key) {
        Assert.INSTANCE.notNull(key, "key不能null");
        return keys.contains(key);
    }

    @Override
    public <T> T get(Object key, Callable<T> callable) {
        T t = super.get(key, callable);
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
        keys.clear();
        logger.debug(loggerPrefix() + ",clear", name);
    }

    @Override
    public boolean invalidate() {
        boolean invalidate = super.invalidate();
        logger.debug(loggerPrefix() + ",invalidate:{}", invalidate);
        return invalidate;
    }

    private String loggerPrefix() {
        return "GradfCaffeineCache,name:{}";
    }
}
