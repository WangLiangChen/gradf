package liangchen.wang.gradf.framework.cluster.configuration;

import liangchen.wang.gradf.framework.cluster.cache.LocalRedisCacheManager;
import liangchen.wang.gradf.framework.cluster.redis.GradfStringRedisTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author LiangChen.Wang
 */
public class CacheManagerAutoConfiguration extends CachingConfigurerSupport {
    @Primary
    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public LocalRedisCacheManager cacheManagerWithRedis(RedisConnectionFactory redisConnectionFactory) {
        GradfStringRedisTemplate redisTemplate = new GradfStringRedisTemplate(redisConnectionFactory);
        return new LocalRedisCacheManager(redisTemplate);
    }

    @Primary
    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public LocalRedisCacheManager cacheManagerWithoutRedis() {
        return new LocalRedisCacheManager();
    }
}
