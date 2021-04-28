package liangchen.wang.gradf.framework.cache.cluster.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import liangchen.wang.gradf.framework.cache.cluster.MultilevelCacheManager;
import liangchen.wang.gradf.framework.cache.cluster.override.RedisCacheManager;
import liangchen.wang.gradf.framework.cache.cluster.redis.RedisCacheCreator;
import liangchen.wang.gradf.framework.cache.cluster.runner.CacheMessageConsumerRunner;
import liangchen.wang.gradf.framework.cache.override.CaffeineCacheManager;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.Executor;

@Configuration(proxyBeanMethods = false)
public class CacheClusterAutoConfiguration {
    @Bean
    MultilevelCacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        CaffeineCacheManager localCacheManager = new CaffeineCacheManager(Caffeine.newBuilder());
        RedisCacheManager distributedCacheManager = new RedisCacheManager(redisTemplate, RedisCacheCreator.INSTANCE.cacheWriter(redisTemplate), RedisCacheConfiguration.defaultCacheConfig());
        return new MultilevelCacheManager(localCacheManager, distributedCacheManager, stringRedisTemplate);
    }

    @Bean
    public ApplicationRunner CacheMessageConsumerRunner(Executor taskExecutor, CacheManager cacheManager) {
        return new CacheMessageConsumerRunner(taskExecutor, cacheManager);
    }
}
