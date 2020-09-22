package liangchen.wang.gradf.framework.cluster.configuration;

import liangchen.wang.crdf.framework.cache.primary.CacheManager;
import liangchen.wang.crdf.framework.cache.primary.CaffeineRedisCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author LiangChen.Wang
 */
public class CacheAutoConfiguration extends CachingConfigurerSupport {
	@Primary
	@Bean
	@ConditionalOnBean(StringRedisTemplate.class)
	public CacheManager cacheManagerWithRedis(StringRedisTemplate stringRedisTemplate) {
		return new CaffeineRedisCacheManager(stringRedisTemplate);
	}
	@Primary
	@Bean
	@ConditionalOnMissingBean(StringRedisTemplate.class)
	public CacheManager cacheManagerWithoutRedis() {
		return new CaffeineRedisCacheManager();
	}
}
