package liangchen.wang.gradf.framework.data.dao;

import com.alibaba.ttl.TransmittableThreadLocal;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.logger.Logger;
import liangchen.wang.gradf.framework.commons.logger.LoggerFactory;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import net.javacrumbs.shedlock.support.LockException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;

/**
 * @author LiangChen.Wang
 */
public abstract class AbstractDBLock implements IDBLock {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDBLock.class);
    private static final String DELETESQL = "delete from gradf_lock where lock_key=?";
    // 持有锁的线程
    private TransmittableThreadLocal<HashSet<String>> lockOwnerThreads = new TransmittableThreadLocal<>();
    private TransmittableThreadLocal<Connection> connectionThreads = new TransmittableThreadLocal<>();
    @Inject
    private DataSource dataSource;

    protected Logger getLogger() {
        return logger;
    }

    protected abstract void executeLockSQL(Connection connection, String lockKey);

    @Override
    public boolean lock(String lockKey) {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        return lock(connection, lockKey);
    }

    @Override
    public void unlock(String lockKey) {
        if (isLockOwnerThread(lockKey)) {
            logger.debug("Lock '{}' returned by:{}", lockKey, Thread.currentThread().getName());
            getLockOwnerThreads().remove(lockKey);
            // 删除对应的数据
            // deleteKey(lockKey);
        } else {
            logger.warn("Lock '" + lockKey + "' attempt to return by: " + Thread.currentThread().getName() + " -- but not owner!", new Exception("stack-trace of wrongful returner"));
        }
    }

    private void deleteKey(String lockKey) {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETESQL)) {
            preparedStatement.setString(1, lockKey);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            logger.error("delete lock_key:{} error:{}", lockKey, e.getMessage());
        }
    }

    @Override
    public void executeInLock(String lockKey, VoidCallback callback) {
        executeInLock(lockKey, (Callback<Void>) () -> {
            callback.execute();
            return null;
        });
    }

    @Override
    public <R> R executeInLock(String lockKey, Callback<R> callback) {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            boolean autoCommit = connection.getAutoCommit();
            if (autoCommit) {
                getLogger().debug("connection autoCommit:{}, 设置为false并显式提交", autoCommit);
                connection.setAutoCommit(false);
                return executeInNonManagedTXLock(connection, lockKey, callback);
            }
            getLogger().debug("connection autoCommit:{}, spring管理的连接事务，spring会做事务的提交或者回滚", autoCommit);
            return executeInManagedTXLock(connection, lockKey, callback);
        } catch (SQLException e) {
            throw new InfoException("getConnection error", e);
        }
    }


    private <R> R executeInNonManagedTXLock(Connection connection, String lockKey, Callback<R> callback) {
        boolean obtainedLock = false;
        try {
            if (StringUtil.INSTANCE.isNotBlank(lockKey)) {
                obtainedLock = lock(connection, lockKey);
            }
            final R result = callback.execute();
            try {
                commitConnection(connection);
            } catch (SQLException e) {
                rollbackConnection(connection);
            }
            return result;
        } catch (Exception e) {
            rollbackConnection(connection);
            throw e;
        } finally {
            try {
                unLock(lockKey, obtainedLock);
            } finally {
                closeConnection(connection);
            }
        }
    }

    private <R> R executeInManagedTXLock(Connection connection, String lockKey, Callback<R> callback) {
        boolean obtainedLock = false;
        try {
            if (StringUtil.INSTANCE.isNotBlank(lockKey)) {
                obtainedLock = lock(connection, lockKey);
            }
            return callback.execute();
        } finally {
            unLock(lockKey, obtainedLock);
        }
    }

    private boolean lock(Connection connection, String lockKey) {
        logger.debug("Lock '{}' is desired by: {}", lockKey, Thread.currentThread().getName());
        // 当前线程存在，则认为当前线程持有锁
        if (isLockOwnerThread(lockKey)) {
            logger.debug("Lock '{}' Is already owned by: {}", lockKey, Thread.currentThread().getName());
            return true;
        }
        // 如果获取到锁，executeLockSQL返回，将锁放入线程，提交事务后才释放
        // 如果获取不到锁则等待
        executeLockSQL(connection, lockKey);
        logger.debug("Lock '{}' given to: {}", lockKey, Thread.currentThread().getName());
        getLockOwnerThreads().add(lockKey);
        return true;
    }

    private void commitConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.commit();
        }
    }

    private void rollbackConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                getLogger().error("Couldn't rollback jdbc connection. ", e);
            }
        }
    }

    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                getLogger().error("Failed to close Connection", e);
            } catch (Throwable e) {
                getLogger().error("Unexpected exception closing Connection.This is often due to a Connection being returned after or during shutdown.", e);
            }
        }
    }

    private void unLock(String lockKey, boolean obtainedLock) {
        if (!obtainedLock) {
            return;
        }
        try {
            unlock(lockKey);
        } catch (LockException le) {
            getLogger().error("Error returning lock: ", le);
        }

    }

    public boolean isLockOwnerThread(String lockKey) {
        return getLockOwnerThreads().contains(lockKey);
    }

    private HashSet<String> getLockOwnerThreads() {
        HashSet<String> threadLocks = lockOwnerThreads.get();
        if (threadLocks == null) {
            threadLocks = new HashSet<>();
            lockOwnerThreads.set(threadLocks);
        }
        return threadLocks;
    }
}
