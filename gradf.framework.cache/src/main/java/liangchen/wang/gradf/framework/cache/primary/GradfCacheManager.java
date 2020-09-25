package liangchen.wang.gradf.framework.cache.primary;

import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 */
public interface GradfCacheManager extends CacheManager {
    @Nullable
    GradfCache getCache(String name, long ttl, TimeUnit timeUnit);

    @Nullable
    Optional<GradfCache> getCacheIfPresent(String name);

    /**
     * 清除所有缓存
     */
    void clear();
}
