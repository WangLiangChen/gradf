package liangchen.wang.gradf.framework.cache.override;

import org.springframework.cache.Cache;

/**
 * @author LiangChen.Wang 2021/3/22
 */
public interface CacheManager extends org.springframework.cache.CacheManager {
    Cache getCache(String cacheName, long ttl);
}
