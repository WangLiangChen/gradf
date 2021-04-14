package liangchen.wang.gradf.framework.cache.configuration;

import liangchen.wang.gradf.framework.cache.cluster.MultilevelCacheManager;
import liangchen.wang.gradf.framework.cache.runner.CacheMessageConsumerRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;

/**
 * @author LiangChen.Wang 2021/4/14
 */
@Configuration(proxyBeanMethods = false)
public class CacheAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager cacheManager(@Nullable RedisTemplate<Object, Object> redisTemplate, @Nullable StringRedisTemplate stringRedisTemplate) {
        return new MultilevelCacheManager(redisTemplate, stringRedisTemplate);
    }

    @Bean
    @ConditionalOnBean(CacheManager.class)
    public ApplicationRunner CacheMessageConsumerRunner(Executor taskExecutor, CacheManager cacheManager) {
        return new CacheMessageConsumerRunner(taskExecutor, cacheManager);
    }
}
