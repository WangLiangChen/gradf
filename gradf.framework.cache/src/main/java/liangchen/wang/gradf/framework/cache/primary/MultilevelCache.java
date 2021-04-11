package liangchen.wang.gradf.framework.cache.primary;

import liangchen.wang.gradf.framework.cache.caffeine.CaffeineCache;
import liangchen.wang.gradf.framework.cache.enumeration.CacheStatus;
import liangchen.wang.gradf.framework.cache.override.Cache;
import liangchen.wang.gradf.framework.cache.redis.CacheMessage;
import liangchen.wang.gradf.framework.cache.redis.RedisCache;
import liangchen.wang.gradf.framework.cache.runner.CacheMessageConsumerRunner;
import liangchen.wang.gradf.framework.commons.json.Json;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.lock.LocalLockUtil;
import liangchen.wang.gradf.framework.commons.lock.LockReader;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StreamOperations;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


/**
 * 根据Cache实现的不同 实现Key级别的过期
 *
 * @author LiangChen.Wang 2021/3/22
 */
public class MultilevelCache extends AbstractValueAdaptingCache implements Cache {
    private final Logger logger = LoggerFactory.getLogger(MultilevelCache.class);
    private final String name;
    private final long ttl;
    private final boolean allowNullValues;
    private final Cache localCache;
    private final Cache distributedCache;
    private final StreamOperations<String, Object, Object> streamOperations;
    private final String loggerPrefix;

    public MultilevelCache(String name, long ttl, boolean allowNullValues, MultilevelCacheManager multilevelCacheManager) {
        super(allowNullValues);
        this.name = name;
        this.ttl = ttl;
        this.allowNullValues = allowNullValues;
        this.localCache = new CaffeineCache(name, ttl, true);
        if (CacheStatus.INSTANCE.isRedisEnable()) {
            this.distributedCache = new RedisCache(name, ttl, allowNullValues, multilevelCacheManager.getRedisTemplate());
            this.streamOperations = multilevelCacheManager.getStringRedisTemplate().opsForStream();
        } else {
            this.distributedCache = null;
            this.streamOperations = null;
        }
        this.loggerPrefix = String.format("MultilevelCache(name:%s,ttl:%s,allowNullValues:%s)", name, ttl, allowNullValues);
        logger.debug(loggerPrefix("Constructor"));
    }

    @Override
    public ValueWrapper get(Object key) {
        logger.debug(loggerPrefix("get", "key"), key);
        // null说明缓存不存在
        ValueWrapper valueWrapper = localCache.get(key);
        logger.debug(loggerPrefix("get from local", "key", "valueWrapper", "value"), key, valueWrapper, null == valueWrapper ? null : JsonUtil.INSTANCE.toJsonString(valueWrapper.get()));
        if (null != valueWrapper) {
            return valueWrapper;
        }
        if (CacheStatus.INSTANCE.isNotRedisEnable() || null == (valueWrapper = distributedCache.get(key))) {
            logger.debug(loggerPrefix("get from remote", "key", "valueWrapper"), key, null);
            return null;
        }
        Object value = valueWrapper.get();
        logger.debug(loggerPrefix("get from remote", "key", "value"), key, JsonUtil.INSTANCE.toJsonString(value));
        // 写入localCache
        localCache.put(key, value);
        return valueWrapper;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        logger.debug(loggerPrefix("get", "key", "type"), key, type);
        // null说明缓存不存在
        ValueWrapper valueWrapper = localCache.get(key);
        logger.debug(loggerPrefix("get from local", "key", "type", "valueWrapper", "value"), key, type, valueWrapper, null == valueWrapper ? null : JsonUtil.INSTANCE.toJsonString(valueWrapper.get()));
        if (null != valueWrapper) {
            return localCache.get(key, type);
        }
        if (CacheStatus.INSTANCE.isNotRedisEnable() || null == distributedCache.get(key)) {
            logger.debug(loggerPrefix("get from remote", "key", "type", "valueWrapper"), key, type, null);
            return null;
        }
        T value = distributedCache.get(key, type);
        logger.debug(loggerPrefix("get from remote", "key", "type", "value"), key, type, JsonUtil.INSTANCE.toJsonString(value));
        // 写入localCache
        localCache.put(key, value);
        return value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        logger.debug(loggerPrefix("get", "key", "valueLoader"), key, valueLoader);
        return LocalLockUtil.INSTANCE.readWriteInReadWriteLock(String.format("%s_%s", this.name, key), () -> {
            ValueWrapper valueWrapper = this.get(key);
            if (null == valueWrapper) {
                logger.debug(loggerPrefix("get with read lock", "key", "valueWrapper"), key, null);
                return null;
            }
            Object value = valueWrapper.get();
            logger.debug(loggerPrefix("get with read lock", "key", "value"), key, JsonUtil.INSTANCE.toJsonString(value));
            return new LockReader.LockValueWrapper<>(ClassBeanUtil.INSTANCE.cast(value));
        }, () -> {
            try {
                T value = valueLoader.call();
                logger.debug(loggerPrefix("get with write lock", "key", "value"), key, JsonUtil.INSTANCE.toJsonString(value));
                this.put(key, value);
                return value;
            } catch (Exception e) {
                throw new ValueRetrievalException(key, valueLoader, e);
            }
        });
    }


    @Override
    public void put(Object key, Object value) {
        logger.debug(loggerPrefix("put", "key", "value"), key, JsonUtil.INSTANCE.toJsonString(value));
        if (CacheStatus.INSTANCE.isRedisEnable()) {
            distributedCache.put(key, value);
        }
        localCache.put(key, value);
    }


    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        logger.debug(loggerPrefix("putIfAbsent", "key", "value"), key, JsonUtil.INSTANCE.toJsonString(value));
        if (CacheStatus.INSTANCE.isRedisEnable()) {
            distributedCache.putIfAbsent(key, value);
        }
        return localCache.putIfAbsent(key, value);

    }

    @Override
    public void evict(Object key) {
        logger.debug(loggerPrefix("evict", "key"), key);
        if (CacheStatus.INSTANCE.isNotRedisEnable()) {
            localCache.evict(key);
            return;
        }
        distributedCache.evict(key);
        // 发送消息
        ObjectRecord<String, CacheMessage> record = StreamRecords.newRecord().ofObject(CacheMessage.newInstance(this.name, CacheMessage.CacheAction.evict, key)).withStreamKey(CacheMessageConsumerRunner.EXPIRE_CHANNEL);
        this.streamOperations.add(record);
    }

    public void evictLocal(Object key) {
        logger.debug(loggerPrefix("evictLocal", "key"), key);
        localCache.evict(key);
    }

    @Override
    public void clear() {
        logger.debug(loggerPrefix("clear"));
        if (CacheStatus.INSTANCE.isNotRedisEnable()) {
            localCache.clear();
            return;
        }
        distributedCache.clear();
        // 发送消息
        ObjectRecord<String, CacheMessage> record = StreamRecords.newRecord().ofObject(CacheMessage.newInstance(this.name, CacheMessage.CacheAction.clear)).withStreamKey(CacheMessageConsumerRunner.EXPIRE_CHANNEL);
        this.streamOperations.add(record);
    }

    public void clearLocal() {
        logger.debug(loggerPrefix("clearLocal"));
        localCache.clear();
    }

    @Override
    public Set<Object> keys() {
        return localCache.keys();
    }

    @Override
    public boolean containsKey(Object key) {
        return localCache.containsKey(key);
    }

    @Override
    public long getTtl() {
        return this.ttl;
    }

    @Override
    protected Object lookup(Object key) {
        // 已覆写get 此处不再被调用
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
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
        return "MultilevelCache{" +
                "name='" + name + '\'' +
                ", ttl=" + ttl +
                ", allowNullValues=" + allowNullValues +
                '}';
    }
}
