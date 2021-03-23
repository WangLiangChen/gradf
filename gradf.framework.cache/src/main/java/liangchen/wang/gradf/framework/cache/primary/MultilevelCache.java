package liangchen.wang.gradf.framework.cache.primary;

import liangchen.wang.gradf.framework.cache.caffeine.CaffeineCache;
import liangchen.wang.gradf.framework.cache.override.Cache;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * 根据Cache实现的不同 实现Key级别的过期
 *
 * @author LiangChen.Wang 2021/3/22
 */
public class MultilevelCache extends AbstractValueAdaptingCache implements Cache {
    private final String name;
    private final Cache localCache;
    private final Cache distributedCache;
    private final long ttl;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public MultilevelCache(String name, long ttl, boolean allowNullValues) {
        super(allowNullValues);
        this.name = name;
        this.localCache = new CaffeineCache(name, ttl, true);
        this.distributedCache = null;
        this.ttl = ttl;
    }

    @Override
    public void put(Object key, Object value, long ttl) {
        // 如果未设置过期 则使用Cache的过期
        if (0 == ttl) {
            ttl = this.ttl;
        }
        if (null != distributedCache) {
            distributedCache.put(key, value, ttl);
        }
        localCache.put(key, value, ttl);
    }

    @Override
    public void put(Object key, Object value) {
        put(key, value, 0L);
    }

    @Override
    public ValueWrapper get(Object key) {
        // null说明缓存不存在
        ValueWrapper valueWrapper = localCache.get(key);
        if (null == valueWrapper && null != distributedCache) {
            valueWrapper = distributedCache.get(key);
        }
        return valueWrapper;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper valueWrapper = get(key);
        if (null == valueWrapper || null == valueWrapper.get()) {
            return null;
        }
        return ClassBeanUtil.INSTANCE.cast(valueWrapper.get());
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return get(key, valueLoader, 0L);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader, long ttl) {
        ValueWrapper valueWrapper = get(key);
        // 第一级 无锁查询
        if (null != valueLoader) {
            return ClassBeanUtil.INSTANCE.cast(valueWrapper.get());
        }
        //第二级 读锁查询
        readLock.lock();
        try {
            valueWrapper = get(key);
            if (null != valueLoader) {
                return ClassBeanUtil.INSTANCE.cast(valueWrapper.get());
            }
            //第三级 释放读锁,加写锁写入
            readLock.unlock();
            writeLock.lock();
            try {
                //二次验证
                valueWrapper = get(key);
                if (null != valueLoader) {
                    return ClassBeanUtil.INSTANCE.cast(valueWrapper.get());
                }
                try {
                    T returnValue = valueLoader.call();
                    put(key, returnValue, ttl);
                    return returnValue;
                } catch (Exception e) {
                    throw new ValueRetrievalException(key, valueLoader, e);
                }
            } finally {
                //锁降级，释放写锁之前,再次获取读锁，防止在写锁释放的瞬间其它写锁修改数据，保证本次写线程读取数据的原子性，保证返回的是本次写入的内容。
                readLock.lock();
                writeLock.unlock();
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void evict(Object key) {
        localCache.evict(key);
        if (null != distributedCache) {
            distributedCache.evict(key);
        }
    }

    @Override
    public void clear() {
        localCache.clear();
        if (null != distributedCache) {
            distributedCache.clear();
        }
    }

    @Override
    public Set<Object> keys() {
        if (null != distributedCache) {
            return distributedCache.keys();
        }
        return localCache.keys();
    }

    @Override
    public boolean containsKey(Object key) {
        if (null != distributedCache) {
            return distributedCache.containsKey(key);
        }
        return localCache.containsKey(key);
    }

    @Override
    public long getTtl() {
        return this.ttl;
    }

    @Override
    protected Object lookup(Object key) {
        // 已覆写get 此处不再被调用
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }
}
