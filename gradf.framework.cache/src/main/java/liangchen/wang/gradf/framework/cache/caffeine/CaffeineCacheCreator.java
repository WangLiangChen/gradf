package liangchen.wang.gradf.framework.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020/9/23
 */
public enum CaffeineCacheCreator {
    //
    INSTANCE;

    public Cache<Object, Object> nativeCaffeineCache(long ttl, TimeUnit timeUnit) {
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if (ttl > 0) {
            cacheBuilder.expireAfterWrite(ttl, timeUnit);
        }
        return cacheBuilder.build();
    }
}
