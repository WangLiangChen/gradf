package liangchen.wang.gradf.framework.cache.override;

import liangchen.wang.gradf.framework.commons.lock.LocalLockUtil;
import liangchen.wang.gradf.framework.commons.lock.LockReader;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author LiangChen.Wang 2021/4/20
 */
public abstract class AbstractCacheManager implements CacheManager, InitializingBean {
    private final String LOCK_KEY = "AbstractCacheManager";
    private boolean transactionAware = false;
    private final ConcurrentMap<String, org.springframework.cache.Cache> cacheMap = new ConcurrentHashMap<>(16);

    @Override
    public void afterPropertiesSet() {
        initializeCaches();
    }

    public void initializeCaches() {
        Collection<? extends org.springframework.cache.Cache> caches = loadCaches();
        synchronized (this.cacheMap) {
            this.cacheMap.clear();
            for (org.springframework.cache.Cache cache : caches) {
                String name = cache.getName();
                this.cacheMap.put(name, decorateCache(cache));
            }
        }
    }

    public void setTransactionAware(boolean transactionAware) {
        this.transactionAware = transactionAware;
    }

    public boolean isTransactionAware() {
        return this.transactionAware;
    }

    protected abstract Collection<? extends org.springframework.cache.Cache> loadCaches();

    @Nullable
    protected final org.springframework.cache.Cache lookupCache(String name) {
        return this.cacheMap.get(name);
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.cacheMap.keySet();
    }

    @Override
    @Nullable
    public org.springframework.cache.Cache getCache(String name) {
        return getCache(name, 0L);
    }

    @Override
    @Nullable
    public org.springframework.cache.Cache getCache(String name, long ttl) {
        String lockKey = String.format("%s::%s", LOCK_KEY, name);
        return LocalLockUtil.INSTANCE.readWriteInReadWriteLock(lockKey, () -> {
            org.springframework.cache.Cache cache = this.cacheMap.get(name);
            if (null == cache) {
                return null;
            }
            return new LockReader.LockValueWrapper<>(cache);
        }, () -> {
            org.springframework.cache.Cache missingCache = getMissingCache(name, ttl);
            if (null == missingCache) {
                return null;
            }
            missingCache = decorateCache(missingCache);
            this.cacheMap.put(name, missingCache);
            return missingCache;
        });
    }

    protected org.springframework.cache.Cache decorateCache(org.springframework.cache.Cache cache) {
        return (isTransactionAware() ? new TransactionAwareCacheDecorator(cache) : cache);
    }

    @Nullable
    protected abstract org.springframework.cache.Cache getMissingCache(String name, long ttl);
}
