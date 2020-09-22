package liangchen.wang.gradf.framework.cluster.configuration;


import liangchen.wang.crdf.framework.cluster.cache.CaffeineRedisCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.Executor;

/**
 * @author LiangChen.Wang
 */
@ConditionalOnBean(RedisConnectionFactory.class)
public class RedisSubscriberAutoConfiguration extends CachingConfigurerSupport {
    public static final String CACHE_SYNCHRONIZATION_CHANNEL = "CrdfCacheSynchronization";

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, Executor executor, CaffeineRedisCacheManager caffeineRedisCacheManager) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(caffeineRedisCacheManager, "receiveRedisMessage");
        messageListenerAdapter.setStringSerializer(new StringRedisSerializer());
        //因为不是spring的bean，所以要显式的调用afterPropertiesSet
        messageListenerAdapter.afterPropertiesSet();

        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, new PatternTopic(CACHE_SYNCHRONIZATION_CHANNEL));
        redisMessageListenerContainer.setTaskExecutor(executor);
        return redisMessageListenerContainer;
    }

}
