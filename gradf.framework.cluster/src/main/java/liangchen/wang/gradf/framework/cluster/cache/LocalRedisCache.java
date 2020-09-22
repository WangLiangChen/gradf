package liangchen.wang.gradf.framework.cluster.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import liangchen.wang.gradf.framework.cache.caffeine.GradfCaffeineCache;
import liangchen.wang.gradf.framework.cache.primary.GradfCache;
import liangchen.wang.gradf.framework.cluster.configuration.RedisSubscriberAutoConfiguration;
import liangchen.wang.gradf.framework.cluster.enumeration.ClusterStatus;
import liangchen.wang.gradf.framework.commons.digest.HashUtil;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class LocalRedisCache extends AbstractValueAdaptingCache {
    private static final Logger logger = LoggerFactory.getLogger(LocalRedisCache.class);
    private final String name;
    private final String rawName;
    private final boolean allowNullValues;
    private final long ttl;
    private final TimeUnit timeUnit;
    private final StringRedisTemplate stringRedisTemplate;
    private final GradfCache localCache;
    private final RedisCache redisCache;

    LocalRedisCache(String name, String rawName, boolean allowNullValues, long ttl, TimeUnit timeUnit, StringRedisTemplate stringRedisTemplate) {
        super(allowNullValues);
        this.name = name;
        this.rawName = rawName;
        this.allowNullValues = allowNullValues;
        this.ttl = ttl;
        this.timeUnit = timeUnit;
        this.stringRedisTemplate = stringRedisTemplate;
        localCache = obtainLocalCache();
        redisCache = obtainRedisCache();
    }

    @Override
    public void put(Object key, Object value) {
        Object md5Key = md5Key(key);
        if (ClusterStatus.INSTANCE.isRedisEnable()) {
            redisCache.putKey(String.valueOf(md5Key), String.valueOf(key));
            logger.debug("写入RedisCache,rawName:{},name:{},rawKey:{},key:{},value:{}", rawName, name, key, md5Key, value);
            redisCache.put(md5Key, value);
        }
        localCache.putKey(String.valueOf(md5Key), String.valueOf(key));
        logger.debug("写入LocalCache,rawName:{},name:{},rawKey:{},key:{},value:{}", rawName, name, key, md5Key, value);
        localCache.put(md5Key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Object md5Key = md5Key(key);
        if (ClusterStatus.INSTANCE.isRedisEnable()) {
            redisCache.putKey(String.valueOf(md5Key), String.valueOf(key));
            logger.debug("写入RedisCache,rawName:{},name:{},OriginalKey:{},Key:{},value:{}", rawName, name, key, md5Key, value);
            ValueWrapper valueWrapper = redisCache.putIfAbsent(md5Key, value);
            // 已存在,不写入,直接返回
            if (null != valueWrapper) {
                return valueWrapper;
            }
            // 直接写入本地缓存
            localCache.putKey(String.valueOf(md5Key), String.valueOf(key));
            logger.debug("写入LocalCache,rawName:{},name:{},OriginalKey:{},Key:{},value:{}", rawName, name, key, md5Key, value);
            localCache.put(md5Key, value);
            return null;
        }
        localCache.putKey(String.valueOf(md5Key), String.valueOf(key));
        logger.debug("写入LocalCache,rawName:{},name:{},OriginalKey:{},Key:{},value:{}", rawName, name, key, md5Key, value);
        ValueWrapper valueWrapper = localCache.putIfAbsent(md5Key, value);
        return valueWrapper;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object md5Key = md5Key(key);
        if (localCache.containsKey(String.valueOf(md5Key))) {
            return getFromLocal(md5Key, key);
        }
        return getFromRemote(md5Key, key);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper valueWrapper = this.get(key);
        if (null == valueWrapper) {
            return null;
        }
        return ClassBeanUtil.INSTANCE.cast(valueWrapper.get());
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object md5Key = md5Key(key);
        if (localCache.containsKey(String.valueOf(md5Key))) {
            return getFromLocal(md5Key, key, valueLoader);
        }
        return getFromRemote(md5Key, key, valueLoader);
    }

    @Override
    public void evict(Object key) {
        evict(key, (m) -> {
            if (ClusterStatus.INSTANCE.isNotRedisEnable()) {
                return;
            }
            String jsonString = JsonUtil.INSTANCE.toJsonString(m);
            logger.debug("发送Redis消息,Channel:{},内容：{}", RedisSubscriberAutoConfiguration.CACHE_SYNCHRONIZATION_CHANNEL, jsonString);
            stringRedisTemplate.convertAndSend(RedisSubscriberAutoConfiguration.CACHE_SYNCHRONIZATION_CHANNEL, jsonString);
        });
    }

    public void evict(Object key, Consumer<CacheMessage> consumer) {
        Object md5Key = md5Key(key);
        localCache.evictKey(String.valueOf(md5Key));
        logger.debug("清除localCache,rawName:{},name:{},OriginalKey:{},Key:{}", rawName, name, key, md5Key);
        localCache.evict(md5Key);
        if (!ClusterStatus.INSTANCE.isRedisEnable()) {
            return;
        }
        redisCache.evictKey(String.valueOf(md5Key));
        logger.debug("清除redisCache,rawName:{},name:{},OriginalKey:{},Key:{}", rawName, name, key, md5Key);
        redisCache.evict(md5Key);
        if (null != consumer) {
            CacheMessage message = new CacheMessage();
            message.setAction(CacheMessage.CacheAction.evict);
            message.setName(this.name);
            message.setKey(md5Key);
            consumer.accept(message);
        }
    }

    @Override
    public void clear() {
        clear((m) -> {
            if (ClusterStatus.INSTANCE.isNotRedisEnable()) {
                return;
            }
            String jsonString = JsonUtil.INSTANCE.toJsonString(m);
            logger.debug("发送Redis消息,Channel:{},内容：{}", RedisSubscriberAutoConfiguration.CACHE_SYNCHRONIZATION_CHANNEL, jsonString);
            stringRedisTemplate.convertAndSend(RedisSubscriberAutoConfiguration.CACHE_SYNCHRONIZATION_CHANNEL, jsonString);
        });
    }

    public void clear(Consumer<CacheMessage> consumer) {
        logger.debug("清除localCache,rawName:{},name:{}", rawName, name);
        localCache.clear();
        if (!ClusterStatus.INSTANCE.isRedisEnable()) {
            return;
        }
        logger.debug("清除redisCache,rawName:{},name:{}", rawName, name);
        redisCache.clear();
        if (null != consumer) {
            CacheMessage message = new CacheMessage();
            message.setAction(CacheMessage.CacheAction.clear);
            message.setName(this.name);
            consumer.accept(message);
        }
    }

    public void evictLocal(Object key) {
        logger.debug("根据Redis消息,清除localCache,rawName:{},name:{},Key:{}", rawName, name, key);
        localCache.evictKey(String.valueOf(key));
        localCache.evict(key);
    }

    public void clearLocal() {
        logger.debug("根据Redis消息,清除localCache,rawName:{},name:{}", rawName, name);
        localCache.clear();
    }

    public Set<String> keys() {
        if (ClusterStatus.INSTANCE.isRedisEnable()) {
            return redisCache.keys();
        }
        return localCache.keys();
    }


    @Override
    public String getName() {
        return this.rawName;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    protected Object lookup(Object key) {
        throw new InfoException("已覆盖get方法,此方法无效");
    }

    private GradfCache obtainLocalCache() {
        return new GradfCaffeineCache(name, allowNullValues, ttl, timeUnit);
    }

    private RedisCache obtainRedisCache() {
        if (ClusterStatus.INSTANCE.isNotRedisEnable()) {
            return null;
        }
        RedisSerializer redisSerializer = StringRedisSerializer.UTF_8;
        RedisSerializationContext.SerializationPair<String> pair = RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeKeysWith(pair).serializeValuesWith(pair);
        RedisCacheWriter cacheWriter = RedisCacheWriter.lockingRedisCacheWriter(stringRedisTemplate.getConnectionFactory());
        if (ttl > 0) {
            long millis = timeUnit.toMillis(ttl);
            Duration duration = Duration.ofMillis(millis);
            //此处会返回一个新的RedisCacheConfiguration
            redisCacheConfiguration = redisCacheConfiguration.entryTtl(duration);
        }
        return new RedisCache(name, cacheWriter, redisCacheConfiguration, stringRedisTemplate);
    }

    private ValueWrapper getFromLocal(Object key, Object rawKey) {
        if (!localCache.containsKey(String.valueOf(key))) {
            return null;
        }
        ValueWrapper valueWrapper = localCache.get(key);
        logger.debug("从localCache取值,rawName:{},name:{},rawKey:{},key:{},value:{}", rawName, name, rawKey, key, valueWrapper == null ? null : valueWrapper.get());
        return valueWrapper;
    }

    private ValueWrapper getFromRemote(Object key, Object rawKey) {
        if (!ClusterStatus.INSTANCE.isRedisEnable() || !redisCache.containsKey(String.valueOf(key))) {
            return null;
        }
        ValueWrapper valueWrapper = redisCache.get(key);
        logger.debug("从redisCache取值,rawName:{},name:{},rawKey:{},key:{},value:{}", rawName, name, rawKey, key, valueWrapper == null ? null : valueWrapper.get());
        if (null == valueWrapper) {
            return null;
        }
        localCache.put(key, valueWrapper.get());
        logger.debug("从redisCache同步localCache,rawName:{},name:{},rawKey:{},key:{},value:{}", rawName, name, rawKey, key, valueWrapper == null ? null : valueWrapper.get());
        // 再次从本地缓存查询 力求最新
        return localCache.get(key);
    }

    private <T> T getFromLocal(Object key, Object rawKey, Callable<T> valueLoader) {
        if (!localCache.containsKey(String.valueOf(key))) {
            return null;
        }
        T value = localCache.get(rawKey, valueLoader);
        logger.debug("从localCache取值,rawName:{},name:{},rawKey:{},key:{},value:{}", rawName, name, rawKey, key, value);
        return value;
    }

    private <T> T getFromRemote(Object key, Object rawKey, Callable<T> valueLoader) {
        if (!ClusterStatus.INSTANCE.isRedisEnable() || !redisCache.containsKey(String.valueOf(key))) {
            return null;
        }
        T value = redisCache.get(key, valueLoader);
        logger.debug("从redisCache取值,rawName:{},name:{},rawKey:{},key:{},value:{}", rawName, name, rawKey, key, value);
        localCache.put(key, value);
        logger.debug("从redisCache同步到localCache,rawName:{},name:{},rawKey:{},key:{},value:{}", rawName, name, rawKey, key, value);
        return localCache.get(key, valueLoader);
    }

    private Object md5Key(Object key) {
        if (!(key instanceof String)) {
            return key;
        }
        String originalKey = String.valueOf(key);
        if (originalKey.length() > 32) {
            originalKey = HashUtil.INSTANCE.md5Digest(originalKey);
        }
        return originalKey;
    }
}
