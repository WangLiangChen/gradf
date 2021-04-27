package liangchen.wang.gradf.framework.cache.cluster.configuration;

import liangchen.wang.gradf.framework.cache.cluster.override.RedisCacheManager;
import liangchen.wang.gradf.framework.cache.cluster.redis.RedisCacheCreator;
import liangchen.wang.gradf.framework.cache.cluster.runner.CacheMessageConsumerRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheAspectSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nullable;
import java.util.concurrent.Executor;

/**
 * @author LiangChen.Wang 2021/4/14
 */

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(CacheManager.class)
@ConditionalOnBean(value = {CacheAspectSupport.class, RedisAutoConfiguration.class})
//@AutoConfigureAfter(CacheAutoConfiguration.class)
public class CacheClusterAutoConfiguration {
    @Bean
    public RedisCacheManager cacheManagerOverride(@Nullable RedisTemplate<Object, Object> redisTemplate, @Nullable StringRedisTemplate stringRedisTemplate) {
        return new RedisCacheManager(redisTemplate, RedisCacheCreator.INSTANCE.cacheWriter(redisTemplate), RedisCacheCreator.INSTANCE.cacheConfig(0L, true));
    }

    //@Bean
    //@ConditionalOnBean(CacheManager.class)
    public ApplicationRunner CacheMessageConsumerRunner(Executor taskExecutor, CacheManager cacheManager) {
        return new CacheMessageConsumerRunner(taskExecutor, cacheManager);
    }
}
