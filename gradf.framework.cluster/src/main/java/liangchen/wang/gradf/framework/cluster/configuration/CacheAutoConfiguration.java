package liangchen.wang.gradf.framework.cluster.configuration;

import liangchen.wang.gradf.framework.cluster.cache.LocalRedisCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author LiangChen.Wang
 */
public class CacheAutoConfiguration extends CachingConfigurerSupport {
    @Primary
    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public CacheManager cacheManagerWithRedis(RedisTemplate<Object, Object> redisTemplate) {
        return new LocalRedisCacheManager(redisTemplate);
    }

    @Primary
    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public CacheManager cacheManagerWithoutRedis() {
        return new LocalRedisCacheManager();
    }
}
