package liangchen.wang.gradf.framework.cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import liangchen.wang.gradf.framework.cache.primary.CacheNameResolver;
import liangchen.wang.gradf.framework.cache.primary.GradfCache;
import liangchen.wang.gradf.framework.cache.primary.GradfCacheManager;
import liangchen.wang.gradf.framework.commons.digest.HashUtil;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.lock.LocalLockUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.Cache;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020/9/21
 */
@ConditionalOnMissingBean(GradfCacheManager.class)
public class GradfCaffeineCacheManager implements GradfCacheManager {
    private final Map<String, GradfCaffeineCache> cacheMap = new ConcurrentHashMap<>(16);

    @Override
    public GradfCache getCache(String name, TimeUnit timeUnit, long ttl) {
        String md5Name = md5Name(name);
        try {
            return LocalLockUtil.INSTANCE.readWriteInReadWriteLock(md5Name, () -> this.cacheMap.get(md5Name), () -> {
                GradfCaffeineCache newCache = obtainCaffeineCache(md5Name, timeUnit, ttl);
                this.cacheMap.put(md5Name, newCache);
                return newCache;
            });
        } catch (Throwable t) {
            throw new ErrorException(t);
        }
    }

    @Override
    public Cache getCache(String name) {
        CacheNameResolver cacheNameResolver = new CacheNameResolver(name);
        return getCache(cacheNameResolver.getName(), cacheNameResolver.getTimeUnit(), cacheNameResolver.getTtl());
    }

    @Override
    public Optional<GradfCache> getCacheIfPresent(String name) {
        CacheNameResolver cacheNameResolver = new CacheNameResolver(name);
        String md5Name = md5Name(cacheNameResolver.getName());
        return Optional.ofNullable(this.cacheMap.get(md5Name));
    }

    @Override
    public void clear() {
        this.cacheMap.forEach((k, v) -> v.clear());
        this.cacheMap.clear();
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }


    private String md5Name(String name) {
        if (name.length() > 32) {
            name = HashUtil.INSTANCE.md5Digest(name);
        }
        return name;
    }

    private GradfCaffeineCache obtainCaffeineCache(String name, TimeUnit timeUnit, long ttl) {
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if (ttl > 0) {
            cacheBuilder.expireAfterWrite(ttl, timeUnit);
        }
        com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCaffeineCache = cacheBuilder.build();
        return new GradfCaffeineCache(name, nativeCaffeineCache);
    }
}
