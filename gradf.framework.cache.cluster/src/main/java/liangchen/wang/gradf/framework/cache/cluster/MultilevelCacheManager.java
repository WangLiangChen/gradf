package liangchen.wang.gradf.framework.cache.cluster;

import liangchen.wang.gradf.framework.cache.override.AbstractCacheManager;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;

/**
 * @author LiangChen.Wang 2021/3/22
 */
public class MultilevelCacheManager extends AbstractCacheManager {
    private final CacheManager localCacheManager, distributedCacheManager;

    public MultilevelCacheManager(CacheManager localCacheManager, CacheManager distributedCacheManager) {
        this.localCacheManager = localCacheManager;
        this.distributedCacheManager = distributedCacheManager;
    }


    @Override
    protected Collection<? extends Cache> loadCaches() {
        return Collections.emptySet();
    }

    @Nullable
    @Override
    protected Cache getMissingCache(String name, long ttl) {
        Cache localCache, distributedCache;
        if (localCacheManager instanceof liangchen.wang.gradf.framework.cache.override.CacheManager) {
            localCache = ((liangchen.wang.gradf.framework.cache.override.CacheManager) localCacheManager).getCache(name, ttl);
        } else {
            localCache = localCacheManager.getCache(name);
        }
        /**
         *  private final Logger logger = LoggerFactory.getLogger(this.getClass());
         *     private final String LOCK_KEY = "MultilevelCacheManager";
         *     private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);
         *     private volatile Set<String> cacheNames = Collections.emptySet();
         *     private final RedisTemplate<Object, Object> redisTemplate;
         *     private final StringRedisTemplate stringRedisTemplate;
         *
         *     public MultilevelCacheManager(RedisTemplate<Object, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
         *         this.redisTemplate = redisTemplate;
         *         this.stringRedisTemplate = stringRedisTemplate;
         *     }
         *
         *
         *     @Override
         *     public Cache getCache(String cacheName, long ttl) {
         *         String lockKey = String.format("%s::%s", LOCK_KEY, cacheName);
         *         return LocalLockUtil.INSTANCE.readWriteInReadWriteLock(lockKey, () -> {
         *             Cache cache = this.cacheMap.get(cacheName);
         *             if (null == cache) {
         *                 logger.debug("getCache with readLock,cacheName:{},cache:null", cacheName);
         *                 return null;
         *             }
         *             logger.debug("getCache with readLock,cacheName:{},cache:{}", cacheName, cache);
         *             return new LockReader.LockValueWrapper<>(cache);
         *         }, () -> {
         *             Cache missingCache = getMissingCache(cacheName, ttl);
         *             logger.debug("getCache with writeLock,cacheName:{},cache:{}", cacheName, missingCache);
         *             if (null == missingCache) {
         *                 return null;
         *             }
         *             missingCache = decorateCache(missingCache);
         *             this.cacheMap.put(cacheName, missingCache);
         *             updateCacheNames(cacheName);
         *             return missingCache;
         *         });
         *     }
         *
         *     private Cache getMissingCache(String cacheName, long ttl) {
         *         return new MultilevelCache(cacheName, ttl, true, this);
         *     }
         *
         *     @Override
         *     public Cache getCache(String name) {
         *         return getCache(name, 0L);
         *     }
         *
         *     @Override
         *     protected Collection<? extends Cache> loadCaches() {
         *         return Collections.emptySet();
         *     }
         *
         *     private void updateCacheNames(String name) {
         *         Set<String> cacheNames = new LinkedHashSet<>(this.cacheNames);
         *         cacheNames.add(name);
         *         this.cacheNames = Collections.unmodifiableSet(cacheNames);
         *     }
         *
         *     public RedisTemplate<Object, Object> getRedisTemplate() {
         *         return redisTemplate;
         *     }
         *
         *     public StringRedisTemplate getStringRedisTemplate() {
         *         return stringRedisTemplate;
         *     }
         */

        return null;
    }
}
