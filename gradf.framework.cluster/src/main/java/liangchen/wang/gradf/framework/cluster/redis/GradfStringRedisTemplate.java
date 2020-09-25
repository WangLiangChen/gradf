package liangchen.wang.gradf.framework.cluster.redis;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author LiangChen.Wang 2020/9/25
 */
public class GradfStringRedisTemplate extends RedisTemplate<Object, Object> {
    public GradfStringRedisTemplate() {
        setKeySerializer(RedisSerializer.string());
        setValueSerializer(RedisSerializer.string());
        setHashKeySerializer(RedisSerializer.string());
        setHashValueSerializer(RedisSerializer.string());
    }

    public GradfStringRedisTemplate(RedisConnectionFactory connectionFactory) {
        this();
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }

    @Override
    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        return new DefaultStringRedisConnection(connection);
    }
}
