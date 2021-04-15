package liangchen.wang.gradf.framework.cache.configuration;

import liangchen.wang.gradf.framework.cache.override.CacheInterceptor;
import liangchen.wang.gradf.framework.cache.override.CachePutOperation;
import liangchen.wang.gradf.framework.cache.override.CacheableOperation;
import liangchen.wang.gradf.framework.cache.override.*;
import liangchen.wang.gradf.framework.commons.digest.HashUtil;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author LiangChen.Wang 2020/9/23
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration.class)
public class CacheAutoConfiguration {
    private final Logger logger = LoggerFactory.getLogger(CacheAutoConfiguration.class);
    private final String NO_PARAM_KEY = "NO_PARAM";
    private final String NULL_PARAM_KEY = "NULL_PARAM";

    @Primary
    @Bean
    public CaffeineCacheManager cacheManagerOverride() {
        return new CaffeineCacheManager();
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
        interceptor.setKeyGenerator(keyGenerator());
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
                if (cacheManager instanceof liangchen.wang.gradf.framework.cache.override.CacheManager) {
                    if (operation instanceof CacheableOperation) {
                        ttl = ((CacheableOperation) operation).getTtl();
                    } else if (operation instanceof CachePutOperation) {
                        ttl = ((CachePutOperation) operation).getTtl();
                    }
                }
                Collection<Cache> result = new ArrayList<>(cacheNames.size());
                for (String cacheName : cacheNames) {
                    Cache cache;
                    if (ttl == 0) {
                        cache = cacheManager.getCache(cacheName);
                    } else {
                        cache = ((liangchen.wang.gradf.framework.cache.override.CacheManager) cacheManager).getCache(cacheName, ttl);
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

    private KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder key = new StringBuilder();
            key.append(target.getClass().getName()).append(".").append(method.getName()).append(":");
            if (params.length == 0) {
                key.append(NO_PARAM_KEY);
                return HashUtil.INSTANCE.md5Digest(key.toString());
            }
            Object param;
            for (int i = 0; i < params.length; i++) {
                if (i > 0) {
                    key.append('-');
                }
                param = params[i];
                if (param == null) {
                    key.append(NULL_PARAM_KEY);
                    continue;
                }
                key.append(JsonUtil.INSTANCE.toJsonStringWithTransientField(param));
            }
            logger.debug("Generated key:{}", key);
            return HashUtil.INSTANCE.md5Digest(key.toString());
        };
    }
}
