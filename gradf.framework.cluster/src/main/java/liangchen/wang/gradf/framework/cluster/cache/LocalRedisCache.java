package liangchen.wang.gradf.framework.cluster.cache;

import liangchen.wang.gradf.framework.cache.caffeine.GradfCaffeineCache;
import liangchen.wang.gradf.framework.cache.primary.GradfCache;
import liangchen.wang.gradf.framework.cluster.configuration.RedisSubscriberAutoConfiguration;
import liangchen.wang.gradf.framework.cluster.enumeration.ClusterStatus;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class LocalRedisCache extends AbstractValueAdaptingCache implements GradfCache {
    private static final Logger logger = LoggerFactory.getLogger(LocalRedisCache.class);
    private final String name;
    private final boolean allowNullValues;
    private final long ttl;
    private final TimeUnit timeUnit;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final GradfCache localCache;
    private final RedisCache remoteCache;

    LocalRedisCache(String name, boolean allowNullValues, long ttl, TimeUnit timeUnit, RedisTemplate<Object, Object> redisTemplate) {
        super(allowNullValues);
        this.name = name;
        this.allowNullValues = allowNullValues;
        this.ttl = ttl;
        this.timeUnit = timeUnit;
        this.redisTemplate = redisTemplate;
        localCache = obtainLocalCache();
        remoteCache = obtainRedisCache();
    }

    @Override
    public void put(Object key, Object value) {
        if (ClusterStatus.INSTANCE.isRedisEnable()) {
            remoteCache.put(key, value);
            logger.debug("写入RemoteCache,name:{},key:{},value:{}", name, key, value);
        }
        localCache.put(key, value);
        logger.debug("写入LocalCache,name:{},key:{},value:{}", name, key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        if (ClusterStatus.INSTANCE.isRedisEnable()) {
            ValueWrapper valueWrapper = remoteCache.putIfAbsent(key, value);
            logger.debug("写入RemoteCache,name:{},key:{},value:{}", name, key, value);
            // 已存在,不写入,直接返回
            if (null != valueWrapper) {
                return valueWrapper;
            }
            // 直接写入本地缓存
            localCache.put(key, value);
            logger.debug("写入LocalCache,name:{},key:{},value:{}", name, key, value);
            return null;
        }
        ValueWrapper valueWrapper = localCache.putIfAbsent(key, value);
        logger.debug("写入LocalCache,name:{},key:{},value:{}", name, key, value);
        return valueWrapper;
    }

    @Override
    public ValueWrapper get(Object key) {
        //先从LocalCache获取，获取不到再去RemoteCache获取;这里不加锁了，无非是有一些请求穿透到RemoteCache
        if (localCache.containsKey(String.valueOf(key))) {
            return getFromLocal(key);
        }
        return getFromRemote(key);
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

        if (localCache.containsKey(String.valueOf(key))) {
            return getFromLocal(key, valueLoader);
        }
        return getFromRemote(key, valueLoader);
    }

    @Override
    public void evict(Object key) {
        evict(key, (message) -> {
            if (ClusterStatus.INSTANCE.isNotRedisEnable()) {
                return;
            }
            String jsonString = JsonUtil.INSTANCE.toJsonString(message);
            logger.debug("发送Redis消息,Channel:{},内容：{}", RedisSubscriberAutoConfiguration.CACHE_SYNCHRONIZATION_CHANNEL, jsonString);
            redisTemplate.convertAndSend(RedisSubscriberAutoConfiguration.CACHE_SYNCHRONIZATION_CHANNEL, jsonString);
        });
    }

    public void evict(Object key, Consumer<CacheMessage> consumer) {
        localCache.evict(key);
        logger.debug("清除LocalCache,name:{},Key:{}", name, key);
        if (ClusterStatus.INSTANCE.isNotRedisEnable()) {
            return;
        }
        remoteCache.evict(key);
        logger.debug("清除RemoteCache,name:{},Key:{}", name, key);
        if (null != consumer) {
            CacheMessage message = new CacheMessage();
            message.setAction(CacheMessage.CacheAction.evict);
            message.setName(this.name);
            message.setKey(key);
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
            redisTemplate.convertAndSend(RedisSubscriberAutoConfiguration.CACHE_SYNCHRONIZATION_CHANNEL, jsonString);
        });
    }

    public void clear(Consumer<CacheMessage> consumer) {
        localCache.clear();
        logger.debug("清除LocalCache,name:{}", name);
        if (ClusterStatus.INSTANCE.isNotRedisEnable()) {
            return;
        }
        remoteCache.clear();
        logger.debug("清除RemoteCache,name:{}", name);
        if (null != consumer) {
            CacheMessage message = new CacheMessage();
            message.setAction(CacheMessage.CacheAction.clear);
            message.setName(this.name);
            consumer.accept(message);
        }
    }

    public void evictLocal(Object key) {
        localCache.evict(key);
        logger.debug("根据Redis消息,清除LocalCache,name:{},Key:{}", name, key);
    }

    public void clearLocal() {
        localCache.clear();
        logger.debug("根据Redis消息,清除LocalCache,name:{}", name);
    }

    @Override
    public Set<Object> keys() {
        if (ClusterStatus.INSTANCE.isRedisEnable()) {
            return remoteCache.keys();
        }
        return localCache.keys();
    }

    @Override
    public boolean containsKey(Object key) {
        if (ClusterStatus.INSTANCE.isRedisEnable()) {
            return remoteCache.containsKey(key);
        }
        return localCache.containsKey(key);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    protected Object lookup(Object key) {
        // 已覆写get方法 此方法不会调用
        return null;
    }

    private GradfCache obtainLocalCache() {
        return new GradfCaffeineCache(name, allowNullValues, ttl, timeUnit);
    }

    private RedisCache obtainRedisCache() {
        if (ClusterStatus.INSTANCE.isNotRedisEnable()) {
            return null;
        }
        return new RedisCache(name, ttl, timeUnit, redisTemplate);
    }

    private ValueWrapper getFromLocal(Object key) {
        ValueWrapper valueWrapper = localCache.get(key);
        logger.debug("从LocalCache取值,name:{},key:{},value:{}", name, key, valueWrapper == null ? null : valueWrapper.get());
        return valueWrapper;
    }

    private ValueWrapper getFromRemote(Object key) {
        if (ClusterStatus.INSTANCE.isNotRedisEnable()) {
            return null;
        }
        ValueWrapper valueWrapper = remoteCache.get(key);
        logger.debug("从RemoteCache取值,name:{},key:{},value:{}", name, key, valueWrapper == null ? null : valueWrapper.get());
        if (null == valueWrapper) {
            return null;
        }
        localCache.put(key, valueWrapper.get());
        logger.debug("从RemoteCache同步LocalCache,name:{},key:{},value:{}", name, key, valueWrapper == null ? null : valueWrapper.get());
        // 再次从本地缓存查询 力求最新
        return localCache.get(key);
    }

    private <T> T getFromLocal(Object key, Callable<T> valueLoader) {
        T value = localCache.get(key, valueLoader);
        logger.debug("从LocalCache取值,name:{},key:{},value:{}", name, key, value);
        return value;
    }

    private <T> T getFromRemote(Object key, Callable<T> valueLoader) {
        if (ClusterStatus.INSTANCE.isNotRedisEnable()) {
            return null;
        }
        T value = remoteCache.get(key, valueLoader);
        logger.debug("从RemoteCache取值,name:{},key:{},value:{}", name, key, value);
        localCache.put(key, value);
        logger.debug("从RemoteCache同步到LocalCache,name:{},key:{},value:{}", name, key, value);
        return localCache.get(key, valueLoader);
    }
}
