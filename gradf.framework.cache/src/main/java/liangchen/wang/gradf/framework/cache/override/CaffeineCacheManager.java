package liangchen.wang.gradf.framework.cache.override;


import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import liangchen.wang.gradf.framework.cache.caffeine.CaffeineCache;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author LiangChen.Wang 2021/4/15
 */
public class CaffeineCacheManager extends AbstractCacheManager {
    @Nullable
    private final Caffeine<Object, Object> cacheBuilder;
    @Nullable
    private final CacheLoader<Object, Object> cacheLoader;
    private final String[] initialCacheNames;

    private boolean allowNullValues = true;
    private boolean dynamic = true;

    public CaffeineCacheManager(String caffeineSpec, String... initialCacheNames) {
        this(caffeineSpec, null, initialCacheNames);
    }

    public CaffeineCacheManager(String caffeineSpec, CacheLoader<Object, Object> cacheLoader, String... initialCacheNames) {
        this(StringUtil.INSTANCE.isBlank(caffeineSpec) ? null : Caffeine.from(caffeineSpec), cacheLoader, initialCacheNames);
    }

    public CaffeineCacheManager(CaffeineSpec caffeineSpec, String... initialCacheNames) {
        this(caffeineSpec, null, initialCacheNames);
    }

    public CaffeineCacheManager(CaffeineSpec caffeineSpec, CacheLoader<Object, Object> cacheLoader, String... initialCacheNames) {
        this(null == caffeineSpec ? null : Caffeine.from(caffeineSpec), cacheLoader, initialCacheNames);
    }

    public CaffeineCacheManager(Caffeine<Object, Object> cacheBuilder, String... initialCacheNames) {
        this(cacheBuilder, null, initialCacheNames);
    }

    public CaffeineCacheManager(Caffeine<Object, Object> cacheBuilder, CacheLoader<Object, Object> cacheLoader, String... initialCacheNames) {
        if (null == cacheBuilder) {
            this.cacheBuilder = Caffeine.newBuilder();
        } else {
            this.cacheBuilder = cacheBuilder;
        }
        this.cacheLoader = cacheLoader;
        this.initialCacheNames = initialCacheNames;
    }


    @Override
    protected Collection<? extends Cache> loadCaches() {
        if (null == this.initialCacheNames || this.initialCacheNames.length == 0) {
            return Collections.emptySet();
        }
        Collection<Cache> caches = new ArrayList<>(initialCacheNames.length);
        for (String initialCacheName : this.initialCacheNames) {
            caches.add(createCaffeineCache(initialCacheName, 0L));
        }
        this.dynamic = false;
        return caches;
    }

    protected CaffeineCache createCaffeineCache(String name, long ttl) {
        if (dynamic) {
            return adaptCaffeineCache(name, createNativeCaffeineCache(ttl));
        }
        return null;
    }

    protected CaffeineCache adaptCaffeineCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache) {
        return new CaffeineCache(name, cache, allowNullValues);
    }

    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(long ttl) {
        if (ttl > 0) {
            cacheBuilder.expireAfterWrite(Duration.ofMillis(ttl));
        } else {
            cacheBuilder.expireAfterWrite(Duration.ZERO);
        }
        return (this.cacheLoader != null ? this.cacheBuilder.build(this.cacheLoader) : this.cacheBuilder.build());
    }

    @Nullable
    @Override
    protected Cache getMissingCache(String name, long ttl) {
        return createCaffeineCache(name, ttl);
    }
}
