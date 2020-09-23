package liangchen.wang.gradf.framework.cache.configuration;

import liangchen.wang.gradf.framework.cache.caffeine.GradfCaffeineCacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

/**
 * @author LiangChen.Wang 2020/9/23
 */
public class CacheAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager gradfCaffeineCacheManager() {
        return new GradfCaffeineCacheManager();
    }
}
