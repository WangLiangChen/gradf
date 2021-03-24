package liangchen.wang.gradf.framework.cache.caffeine;

import liangchen.wang.gradf.framework.cache.override.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author LiangChen.Wang
 */
public class CaffeineCache implements Cache {
    private final static Logger logger = LoggerFactory.getLogger(CaffeineCache.class);
    private final org.springframework.cache.caffeine.CaffeineCache springCaffeineCache;
    private final String name;
    private final long ttl;
    private final boolean allowNullValues;
    private final Set<Object> keys;
    private final String loggerPrefix;

    public CaffeineCache(String name, long ttl, boolean allowNullValues) {
        springCaffeineCache = new org.springframework.cache.caffeine.CaffeineCache(name, CaffeineCacheCreator.INSTANCE.nativeCaffeineCache(ttl, TimeUnit.MILLISECONDS), allowNullValues);
        this.name = name;
        this.ttl = ttl;
        this.allowNullValues = allowNullValues;
        this.keys = new ConcurrentSkipListSet<>();
        this.loggerPrefix = String.format("Cache(name:%s,ttl:%s,allowNullValues:%s)", name, ttl, allowNullValues);
        logger.debug(loggerPrefix("Constructor"));
    }


    @Override
    public <T> T get(Object key, Callable<T> valueLoader, long ttl) {
        logger.debug(loggerPrefix("get", "key", "valueLoader", "ttl"), key, valueLoader, ttl);
        return springCaffeineCache.get(key, valueLoader);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return this.get(key, valueLoader, 0L);
    }

    @Override
    public ValueWrapper get(Object key) {
        logger.debug(loggerPrefix("get", "key"), key);
        return springCaffeineCache.get(key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        logger.debug(loggerPrefix("get", "key", "type"), key, type);
        return springCaffeineCache.get(key, type);
    }


    @Override
    public void put(Object key, Object value, long ttl) {
        logger.debug(loggerPrefix("put", "key", "value", "ttl"), key, value, ttl);
        springCaffeineCache.put(key, value);
        keys.add(key);
    }

    @Override
    public void put(Object key, Object value) {
        this.put(key, value, 0L);
    }

    @Override
    public void evict(Object key) {
        logger.debug(loggerPrefix("evict", "key"), key);
        springCaffeineCache.evict(key);
        keys.remove(key);
    }

    @Override
    public void clear() {
        logger.debug(loggerPrefix("clear"));
        springCaffeineCache.clear();
        keys.clear();
    }

    @Override
    public Set<Object> keys() {
        return this.keys;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.keys.contains(key);
    }

    @Override
    public long getTtl() {
        return this.ttl;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.springCaffeineCache;
    }

    private String loggerPrefix(String method, String... args) {
        String suffix = Arrays.stream(args).map(e -> String.format("%s:{}", e)).collect(Collectors.joining(","));
        if (null == suffix || suffix.length() == 0) {
            return String.format("%s\r\n - Method(name:%s)", loggerPrefix, method);
        }
        return String.format("%s\r\n - Method(name:%s,%s)", loggerPrefix, method, suffix);
    }
}
