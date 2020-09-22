package liangchen.wang.gradf.framework.cluster.redis;

import liangchen.wang.crdf.framework.commons.exeception.ErrorException;
import liangchen.wang.crdf.framework.springboot.api.ILock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.concurrent.locks.Lock;

@Component("Crdf_Distributed_RedisLock")
@ConditionalOnBean(RedisLockRegistry.class)
public class RedisLockImpl implements ILock {
    private final RedisLockRegistry redisLockRegistry;

    @Inject
    public RedisLockImpl(RedisLockRegistry redisLockRegistry) {
        this.redisLockRegistry = redisLockRegistry;
    }

    @Override
    public boolean lock(String lockKey) {
        Lock lock = redisLockRegistry.obtain(lockKey);
        lock.lock();
        return true;
    }

    @Override
    public void unlock(String lockKey) {
        Lock lock = redisLockRegistry.obtain(lockKey);
        lock.unlock();
    }

    @Override
    public void executeInLock(String lockKey, VoidCallback callback) {
        Lock lock = redisLockRegistry.obtain(lockKey);
        lock.lock();
        try {
            callback.execute();
        } catch (Exception e) {
            throw new ErrorException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public <R> R executeInLock(String lockKey, Callback<R> callback) {
        Lock lock = redisLockRegistry.obtain(lockKey);
        lock.lock();
        try {
            return callback.execute();
        } catch (Exception e) {
            throw new ErrorException(e);
        } finally {
            lock.unlock();
        }
    }
}
