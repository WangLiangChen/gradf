package liangchen.wang.gradf.framework.cache.utils;

import liangchen.wang.gradf.framework.springboot.context.BeanLoader;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * @author LiangChen.Wang 2021/5/26
 */
public enum CacheUtil {
    // instance
    INSTANCE;

    public CacheManager cacheManager() {
        return BeanLoader.INSTANCE.getBean("cacheManager");
    }

    public Cache cache(String cacheName) {
        return cacheManager().getCache(cacheName);
    }

    public Cache cache(String cacheName, long ttl) {
        CacheManager cacheManager = cacheManager();
        if (cacheManager instanceof liangchen.wang.gradf.framework.cache.override.CacheManager) {
            return ((liangchen.wang.gradf.framework.cache.override.CacheManager) cacheManager).getCache(cacheName, ttl);
        }
        throw new RuntimeException("cacheManager must be liangchen.wang.gradf.framework.cache.override.CacheManager");
    }

}
