package liangchen.wang.gradf.framework.cache.mlt;

import liangchen.wang.gradf.framework.cache.enumeration.CacheClusterStatus;
import liangchen.wang.gradf.framework.cache.override.AbstractCacheManager;
import liangchen.wang.gradf.framework.cache.override.Cache;
import liangchen.wang.gradf.framework.cache.override.CacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

/**
 * @author LiangChen.Wang 2021/3/22
 */
public class MultilevelCacheManager extends AbstractCacheManager {
    private final CacheManager localCacheManager, distributedCacheManager;
    private final StringRedisTemplate stringRedisTemplate;

    public MultilevelCacheManager(CacheManager localCacheManager, CacheManager distributedCacheManager, StringRedisTemplate stringRedisTemplate) {
        this.localCacheManager = localCacheManager;
        this.distributedCacheManager = distributedCacheManager;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return Collections.emptySet();
    }

    @Nullable
    @Override
    protected Cache getMissingCache(String name, long ttl) {
        return new MultilevelCache(name, ttl, this);
    }

    public Cache getLocalCache(String name, long ttl) {
        return localCacheManager.getCache(name, ttl);
    }

    public Cache getDistributedCache(String name, long ttl) {
        if (CacheClusterStatus.INSTANCE.isDistributedCacheEnable()) {
            return distributedCacheManager.getCache(name, ttl);
        }
        return null;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

}
