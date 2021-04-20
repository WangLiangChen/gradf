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
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    @Override
    public void afterPropertiesSet() {
        initializeCaches();
    }

    public void initializeCaches() {
        Collection<? extends Cache> caches = loadCaches();
        synchronized (this.cacheMap) {
            this.cacheMap.clear();
            for (Cache cache : caches) {
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

    protected abstract Collection<? extends Cache> loadCaches();

    @Nullable
    protected final Cache lookupCache(String name) {
        return this.cacheMap.get(name);
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.cacheMap.keySet();
    }

    @Override
    @Nullable
    public Cache getCache(String name) {
        return getCache(name, 0L);
    }

    @Override
    @Nullable
    public Cache getCache(String name, long ttl) {
        String lockKey = String.format("%s::%s", LOCK_KEY, name);
        return LocalLockUtil.INSTANCE.readWriteInReadWriteLock(lockKey, () -> {
            Cache cache = this.cacheMap.get(name);
            if (null == cache) {
                return null;
            }
            return new LockReader.LockValueWrapper<>(cache);
        }, () -> {
            Cache missingCache = getMissingCache(name, ttl);
            if (null == missingCache) {
                return null;
            }
            missingCache = decorateCache(missingCache);
            this.cacheMap.put(name, missingCache);
            return missingCache;
        });
    }

    protected Cache decorateCache(Cache cache) {
        return (isTransactionAware() ? new TransactionAwareCacheDecorator(cache) : cache);
    }

    @Nullable
    protected abstract Cache getMissingCache(String name, long ttl);
}
