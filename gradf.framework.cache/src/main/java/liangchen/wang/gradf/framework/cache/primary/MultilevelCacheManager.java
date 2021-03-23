package liangchen.wang.gradf.framework.cache.primary;

import liangchen.wang.gradf.framework.cache.caffeine.CaffeineCache;
import liangchen.wang.gradf.framework.cache.caffeine.CaffeineCacheCreator;
import liangchen.wang.gradf.framework.cache.override.CacheManager;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author LiangChen.Wang 2021/3/22
 */
public class MultilevelCacheManager extends AbstractTransactionSupportingCacheManager implements CacheManager {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);
    private volatile Set<String> cacheNames = Collections.emptySet();

    @Override
    public Cache getCache(String cacheName, long ttl) {
        Cache cache = this.cacheMap.get(cacheName);
        if (cache != null) {
            return cache;
        }
        //第二级 读锁查询
        readLock.lock();
        try {
            cache = this.cacheMap.get(cacheName);
            if (cache != null) {
                return cache;
            }
            //第三级 释放读锁,加写锁写入
            readLock.unlock();
            writeLock.lock();
            try {
                //二次验证
                cache = this.cacheMap.get(cacheName);
                if (cache != null) {
                    return cache;
                }
                Cache missingCache = getMissingCache(cacheName, ttl);
                if (null == missingCache) {
                    return null;
                }
                missingCache = decorateCache(missingCache);
                this.cacheMap.put(cacheName, missingCache);
                updateCacheNames(cacheName);
                return missingCache;
            } finally {
                //锁降级，释放写锁之前,再次获取读锁，防止在写锁释放的瞬间其它写锁修改数据，保证本次写线程读取数据的原子性，保证返回的是本次写入的内容。
                readLock.lock();
                writeLock.unlock();
            }
        } finally {
            readLock.unlock();
        }
    }

    private Cache getMissingCache(String cacheName, long ttl) {
        return new MultilevelCache(cacheName, ttl, true);
    }

    @Override
    public Cache getCache(String name) {
        return getCache(name, 0L);
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return null;
    }

    private void updateCacheNames(String name) {
        Set<String> cacheNames = new LinkedHashSet<>(this.cacheNames);
        cacheNames.add(name);
        this.cacheNames = Collections.unmodifiableSet(cacheNames);
    }

}
