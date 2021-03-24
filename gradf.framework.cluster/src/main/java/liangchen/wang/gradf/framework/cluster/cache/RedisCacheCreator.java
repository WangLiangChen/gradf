package liangchen.wang.gradf.framework.cluster.cache;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author LiangChen.Wang 2020/9/23
 */
public enum RedisCacheCreator {
    //
    INSTANCE;

    public RedisCacheWriter cacheWriter(RedisTemplate redisTemplate) {
        return RedisCacheWriter.lockingRedisCacheWriter(redisTemplate.getConnectionFactory());
    }

    public RedisCacheConfiguration cacheConfig(long ttl, boolean allowNullValues) {
        RedisSerializer<String> redisSerializer = StringRedisSerializer.UTF_8;
        RedisSerializationContext.SerializationPair<String> pair = RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeKeysWith(pair).serializeValuesWith(pair);
        if (ttl > 0) {
            Duration duration = Duration.ofMillis(ttl);
            //此处会返回一个新的RedisCacheConfiguration
            redisCacheConfiguration = redisCacheConfiguration.entryTtl(duration);
        }
        if (!allowNullValues) {
            redisCacheConfiguration = redisCacheConfiguration.disableCachingNullValues();
        }
        return redisCacheConfiguration;
    }
}
