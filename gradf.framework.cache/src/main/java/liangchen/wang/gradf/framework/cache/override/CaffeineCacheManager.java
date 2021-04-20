package liangchen.wang.gradf.framework.cache.override;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import liangchen.wang.gradf.framework.cache.caffeine.CaffeineCache;
import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2021/4/15
 */
public class CaffeineCacheManager implements CacheManager {
    private Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();

    @Nullable
    private CacheLoader<Object, Object> cacheLoader;

    private boolean allowNullValues = true;

    private boolean dynamic = true;

    private final Map<String, org.springframework.cache.Cache> cacheMap = new ConcurrentHashMap<>(16);

    private final Collection<String> customCacheNames = new CopyOnWriteArrayList<>();


    /**
     * Construct a dynamic CaffeineCacheManager,
     * lazily creating cache instances as they are being requested.
     */
    public CaffeineCacheManager() {
    }

    /**
     * Construct a static CaffeineCacheManager,
     * managing caches for the specified cache names only.
     */
    public CaffeineCacheManager(String... cacheNames) {
        setCacheNames(Arrays.asList(cacheNames));
    }


    /**
     * Specify the set of cache names for this CacheManager's 'static' mode.
     * <p>The number of caches and their names will be fixed after a call to this method,
     * with no creation of further cache regions at runtime.
     * <p>Calling this with a {@code null} collection argument resets the
     * mode to 'dynamic', allowing for further creation of caches again.
     */
    public void setCacheNames(@Nullable Collection<String> cacheNames) {
        if (cacheNames != null) {
            for (String name : cacheNames) {
                this.cacheMap.put(name, createCaffeineCache(name, 0L));
            }
            this.dynamic = false;
        } else {
            this.dynamic = true;
        }
    }

    /**
     * Set the Caffeine to use for building each individual
     * {@link org.springframework.cache.caffeine.CaffeineCache} instance.
     *
     * @see #createNativeCaffeineCache
     * @see com.github.benmanes.caffeine.cache.Caffeine#build()
     */
    public void setCaffeine(Caffeine<Object, Object> caffeine) {
        Assert.notNull(caffeine, "Caffeine must not be null");
        doSetCaffeine(caffeine);
    }

    /**
     * Set the {@link CaffeineSpec} to use for building each individual
     * {@link org.springframework.cache.caffeine.CaffeineCache} instance.
     *
     * @see #createNativeCaffeineCache
     * @see com.github.benmanes.caffeine.cache.Caffeine#from(CaffeineSpec)
     */
    public void setCaffeineSpec(CaffeineSpec caffeineSpec) {
        doSetCaffeine(Caffeine.from(caffeineSpec));
    }

    /**
     * Set the Caffeine cache specification String to use for building each
     * individual {@link org.springframework.cache.caffeine.CaffeineCache} instance. The given value needs to
     * comply with Caffeine's {@link CaffeineSpec} (see its javadoc).
     *
     * @see #createNativeCaffeineCache
     * @see com.github.benmanes.caffeine.cache.Caffeine#from(String)
     */
    public void setCacheSpecification(String cacheSpecification) {
        doSetCaffeine(Caffeine.from(cacheSpecification));
    }

    private void doSetCaffeine(Caffeine<Object, Object> cacheBuilder) {
        if (!ObjectUtils.nullSafeEquals(this.cacheBuilder, cacheBuilder)) {
            this.cacheBuilder = cacheBuilder;
            refreshCommonCaches();
        }
    }

    /**
     * Set the Caffeine CacheLoader to use for building each individual
     * {@link org.springframework.cache.caffeine.CaffeineCache} instance, turning it into a LoadingCache.
     *
     * @see #createNativeCaffeineCache
     * @see com.github.benmanes.caffeine.cache.Caffeine#build(CacheLoader)
     * @see com.github.benmanes.caffeine.cache.LoadingCache
     */
    public void setCacheLoader(CacheLoader<Object, Object> cacheLoader) {
        if (!ObjectUtils.nullSafeEquals(this.cacheLoader, cacheLoader)) {
            this.cacheLoader = cacheLoader;
            refreshCommonCaches();
        }
    }

    /**
     * Specify whether to accept and convert {@code null} values for all caches
     * in this cache manager.
     * <p>Default is "true", despite Caffeine itself not supporting {@code null} values.
     * An internal holder object will be used to store user-level {@code null}s.
     */
    public void setAllowNullValues(boolean allowNullValues) {
        if (this.allowNullValues != allowNullValues) {
            this.allowNullValues = allowNullValues;
            refreshCommonCaches();
        }
    }

    /**
     * Return whether this cache manager accepts and converts {@code null} values
     * for all of its caches.
     */
    public boolean isAllowNullValues() {
        return this.allowNullValues;
    }


    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }

    @Override
    @Nullable
    public org.springframework.cache.Cache getCache(String name) {
        return getCache(name, 0L);
    }

    @Override
    @Nullable
    public org.springframework.cache.Cache getCache(String name, long ttl) {
        return this.cacheMap.computeIfAbsent(name, cacheName ->
                this.dynamic ? createCaffeineCache(cacheName, ttl) : null);
    }


    /**
     * Register the given native Caffeine Cache instance with this cache manager,
     * adapting it to Spring's cache API for exposure through {@link #getCache}.
     * Any number of such custom caches may be registered side by side.
     * <p>This allows for custom settings per cache (as opposed to all caches
     * sharing the common settings in the cache manager's configuration) and
     * is typically used with the Caffeine builder API:
     * {@code registerCustomCache("myCache", Caffeine.newBuilder().maximumSize(10).build())}
     * <p>Note that any other caches, whether statically specified through
     * {@link #setCacheNames} or dynamically built on demand, still operate
     * with the common settings in the cache manager's configuration.
     *
     * @param name  the name of the cache
     * @param cache the custom Caffeine Cache instance to register
     * @see #adaptCaffeineCache
     * @since 5.2.8
     */
    public void registerCustomCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache) {
        this.customCacheNames.add(name);
        this.cacheMap.put(name, adaptCaffeineCache(name, cache));
    }

    /**
     * Adapt the given new native Caffeine Cache instance to Spring's {@link org.springframework.cache.Cache}
     * abstraction for the specified cache name.
     *
     * @param name  the name of the cache
     * @param cache the native Caffeine Cache instance
     * @return the Spring CaffeineCache adapter (or a decorator thereof)
     * @see org.springframework.cache.caffeine.CaffeineCache
     * @see #isAllowNullValues()
     * @since 5.2.8
     */
    protected org.springframework.cache.Cache adaptCaffeineCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache) {
        return new liangchen.wang.gradf.framework.cache.caffeine.CaffeineCache(name, cache, isAllowNullValues());
    }

    /**
     * Build a common {@link CaffeineCache} instance for the specified cache name,
     * using the common Caffeine configuration specified on this cache manager.
     * <p>Delegates to {@link #adaptCaffeineCache} as the adaptation method to
     * Spring's cache abstraction (allowing for centralized decoration etc),
     * passing in a freshly built native Caffeine Cache instance.
     *
     * @param name the name of the cache
     * @return the Spring CaffeineCache adapter (or a decorator thereof)
     * @see #adaptCaffeineCache
     * @see #createNativeCaffeineCache
     */
    protected org.springframework.cache.Cache createCaffeineCache(String name, long ttl) {
        return adaptCaffeineCache(name, createNativeCaffeineCache(ttl));
    }

    /**
     * Build a common Caffeine Cache instance for the specified cache name,
     * using the common Caffeine configuration specified on this cache manager.
     *
     * @return the native Caffeine Cache instance
     * @see #createCaffeineCache
     */
    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(long ttl) {
        if (ttl > 0) {
            cacheBuilder.expireAfterWrite(ttl, TimeUnit.MILLISECONDS);
        } else {
            cacheBuilder.expireAfterWrite(Duration.ZERO);
        }
        return (this.cacheLoader != null ? this.cacheBuilder.build(this.cacheLoader) : this.cacheBuilder.build());
    }

    /**
     * Recreate the common caches with the current state of this manager.
     */
    private void refreshCommonCaches() {
        for (Map.Entry<String, org.springframework.cache.Cache> entry : this.cacheMap.entrySet()) {
            String key = entry.getKey();
            if (this.customCacheNames.contains(key)) {
                continue;
            }
            Cache value = entry.getValue();
            long ttl = 0;
            if (value instanceof liangchen.wang.gradf.framework.cache.override.Cache) {
                ttl = ((liangchen.wang.gradf.framework.cache.override.Cache) value).getTtl();
            }
            entry.setValue(createCaffeineCache(key, ttl));
        }
    }
}
