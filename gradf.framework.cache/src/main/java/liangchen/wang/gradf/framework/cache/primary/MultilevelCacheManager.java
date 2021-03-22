package liangchen.wang.gradf.framework.cache.primary;

import liangchen.wang.gradf.framework.cache.override.CacheManager;
import org.springframework.cache.Cache;

import java.util.Collection;

/**
 * @author LiangChen.Wang 2021/3/22
 */
public class MultilevelCacheManager implements CacheManager {
    @Override
    public Cache getCache(String cacheName, long ttl) {
        return null;
    }

    @Override
    public Cache getCache(String name) {
        return null;
    }

    @Override
    public Collection<String> getCacheNames() {
        return null;
    }
}
