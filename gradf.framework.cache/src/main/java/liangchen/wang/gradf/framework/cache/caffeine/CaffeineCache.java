package liangchen.wang.gradf.framework.cache.caffeine;

import liangchen.wang.gradf.framework.cache.override.Cache;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;
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
        this.keys = new CopyOnWriteArraySet<>();
        this.loggerPrefix = String.format("CaffeineCache(name:%s,ttl:%s,allowNullValues:%s)", name, ttl, allowNullValues);
        logger.debug(loggerPrefix("Constructor"));
    }


    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        logger.debug(loggerPrefix("get", "key", "valueLoader"), key, valueLoader);
        T value = springCaffeineCache.get(key, valueLoader);
        logger.debug(loggerPrefix("get", "key", "value"), key, JsonUtil.INSTANCE.toJsonString(value));
        return value;
    }

    @Override
    public ValueWrapper get(Object key) {
        logger.debug(loggerPrefix("get", "key"), key);
        ValueWrapper valueWrapper = springCaffeineCache.get(key);
        logger.debug(loggerPrefix("get", "key", "valueWrapper", "value"), key, valueWrapper, null == valueWrapper ? null : JsonUtil.INSTANCE.toJsonString(valueWrapper.get()));
        return valueWrapper;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        logger.debug(loggerPrefix("get", "key", "type"), key, type);
        T value = springCaffeineCache.get(key, type);
        logger.debug(loggerPrefix("get", "key", "type", "value"), key, type, JsonUtil.INSTANCE.toJsonString(value));
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        logger.debug(loggerPrefix("put", "key", "value"), key, JsonUtil.INSTANCE.toJsonString(value));
        springCaffeineCache.put(key, value);
        keys.add(key);
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

    @Override
    public String toString() {
        return "CaffeineCache{" +
                "name='" + name + '\'' +
                ", ttl=" + ttl +
                ", allowNullValues=" + allowNullValues +
                '}';
    }
}
