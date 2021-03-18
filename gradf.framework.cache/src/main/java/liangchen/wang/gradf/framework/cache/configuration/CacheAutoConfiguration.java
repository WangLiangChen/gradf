package liangchen.wang.gradf.framework.cache.configuration;

import liangchen.wang.gradf.framework.cache.caffeine.GradfCaffeineCacheManager;
import liangchen.wang.gradf.framework.cache.parser.SpringCacheAnnotationParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.cache.interceptor.CacheOperationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;

/**
 * @author LiangChen.Wang 2020/9/23
 */
public class CacheAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager gradfCaffeineCacheManager() {
        return new GradfCaffeineCacheManager();
    }

    @Primary
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheOperationSource cacheOperationSourceOverride() {
        return new AnnotationCacheOperationSource(new SpringCacheAnnotationParser());
    }
}
