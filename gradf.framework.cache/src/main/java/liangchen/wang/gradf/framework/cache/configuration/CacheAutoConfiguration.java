package liangchen.wang.gradf.framework.cache.configuration;

import liangchen.wang.gradf.framework.cache.caffeine.GradfCaffeineCacheManager;
import liangchen.wang.gradf.framework.cache.override.CacheInterceptor;
import liangchen.wang.gradf.framework.cache.override.CacheableOperation;
import liangchen.wang.gradf.framework.cache.override.SpringCacheAnnotationParser;
import liangchen.wang.gradf.framework.cache.primary.GradfCacheManager;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.cache.interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

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

    @Primary
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CacheInterceptor cacheInterceptorOverride(CacheOperationSource cacheOperationSource, CacheManager cacheManager) {
        CacheInterceptor interceptor = new CacheInterceptor();
        interceptor.setCacheManager(cacheManager);
        interceptor.setCacheResolver(cacheResolver(cacheManager));
        interceptor.setCacheOperationSource(cacheOperationSource);
        return interceptor;
    }

    private CacheResolver cacheResolver(CacheManager cacheManager) {
        return new SimpleCacheResolver(cacheManager) {
            @Override
            public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
                Collection<String> cacheNames = getCacheNames(context);
                if (cacheNames == null) {
                    return Collections.emptyList();
                }
                BasicOperation operation = context.getOperation();
                long ttl = 0;
                if (operation instanceof CacheableOperation && cacheManager instanceof GradfCacheManager) {
                    ttl = ((CacheableOperation) operation).getTtl();
                }
                Collection<Cache> result = new ArrayList<>(cacheNames.size());
                for (String cacheName : cacheNames) {
                    Cache cache;
                    if (ttl == 0) {
                        cache = cacheManager.getCache(cacheName);
                    } else {
                        cache = ((GradfCacheManager) cacheManager).getCache(cacheName, ttl, TimeUnit.MILLISECONDS);
                    }
                    if (cache == null) {
                        throw new IllegalArgumentException("Cannot find cache named '" + cacheName + "' for " + operation);
                    }
                    result.add(cache);
                }
                return result;
            }
        };
    }
}
