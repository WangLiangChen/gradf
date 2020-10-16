package liangchen.wang.gradf.framework.commons.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author LiangChen.Wang
 */
public enum ContextUtil {
    /*
     * ThreadLocal必须new一个对象自己使用，它并不是为了解决线程安全而设计的，使用它只是为了在同一线程中传递变量
     * TransmittableThreadLocal 只适用父线程到子线程的值传递，不适用线程池子线程之间的值传递
     */
    INSTANCE;
    private final TransmittableThreadLocal<Map<String, Object>> threadLocal = new TransmittableThreadLocal<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    private final String OPERATOR = "OPERATOR";
    private final String OPERATOR_NAME = "OPERATOR_NAME";
    private final String IP = "IP";

    public void setOperator(Long operator) {
        put(OPERATOR, operator);
    }

    public Long getOperator() {
        return get(OPERATOR);
    }

    public void setOperatorName(String operatorName) {
        put(OPERATOR_NAME, operatorName);
    }

    public String getOperatorName() {
        return get(OPERATOR_NAME);
    }

    public void setIp(String ip) {
        put(IP, ip);
    }

    public String getIp() {
        return get(IP);
    }

    public void put(String key, Object value) {
        Map<String, Object> store = loadStore();
        store.put(key, value);
    }

    public <T> T get(String key) {
        Map<String, Object> store = loadStore();
        Object object = store.get(key);
        return ClassBeanUtil.INSTANCE.cast(object);
    }

    public void clear() {
        Map<String, Object> store = loadStore();
        store.clear();
    }

    public void remove() {
        threadLocal.remove();
    }

    /**
     * 读写锁二次验证
     *
     * @return Map<String, Object>
     */
    private Map<String, Object> loadStore() {
        //无锁判断
        Map<String, Object> store = threadLocal.get();
        if (null != store) {
            return store;
        }
        readLock.lock();
        try {
            store = threadLocal.get();
            if (null != store) {
                return store;
            }
            // Must release read lock before acquiring write lock
            readLock.unlock();
            writeLock.lock();
            try {
                // Recheck state because another thread might have acquired write lock and changed state before we did.
                store = threadLocal.get();
                if (null != store) {
                    return store;
                }
                store = new HashMap<>();
                threadLocal.set(store);
                return store;
            } finally {
                // Downgrade by acquiring read lock before releasing write lock
                readLock.lock();
                // Unlock write, still hold read
                writeLock.unlock();
            }
        } finally {
            readLock.unlock();
        }
    }


}
