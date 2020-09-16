package liangchen.wang.gradf.framework.data.dao.impl;

import liangchen.wang.crdf.framework.data.condition.DataConditionAnnotation;
import liangchen.wang.crdf.framework.data.dao.AbstractDBLock;
import net.javacrumbs.shedlock.support.LockException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

@DataConditionAnnotation
@Repository("Crdf_Data_UpdateLockLock")
public class UpdateLockImpl extends AbstractDBLock {
    private int maxRetry = 3;
    private long retryPeriod = 500L;

    private final String UPDATE_FOR_LOCK = "update crdf_lock set lock_datetime=? where lock_key=?";
    private final String INSERT_LOCK = "insert into crdf_lock values(?,?,?,?)";

    @Override
    protected void executeLockSQL(final Connection connection, final String lockName) {
        SQLException lastException = null;
        for (int i = 0; i < maxRetry; i++) {
            try {
                if (!lockViaUpdate(connection, lockName, UPDATE_FOR_LOCK)) {
                    // 当数据不存在时，线程并不会等待，所有线程都会到达这里;
                    // 当多个线程都到这里执行insert时，第一个进入的线程会等待，其它线程会产生死锁异常，然后第一个进入线程会执行成功
                    lockViaInsert(connection, lockName, INSERT_LOCK);
                }
                return;
            } catch (SQLException e) {
                lastException = e;
                if ((i + 1) == maxRetry) {
                    getLogger().debug("Lock '{}' was not obtained by: {}, cause: {}", lockName, Thread.currentThread().getName(), e.getMessage());
                } else {
                    getLogger().debug("Lock '{}' was not obtained by: {} - will try again. cause: {}", lockName, Thread.currentThread().getName(), e.getMessage());
                }
                try {
                    Thread.sleep(retryPeriod);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new LockException("Failure obtaining db row lock: " + lastException.getMessage(), lastException);
    }

    private boolean lockViaUpdate(Connection connection, String lockName, String sql) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql);
        try {
            ps.setObject(1, LocalDateTime.now());
            ps.setString(2, lockName);
            getLogger().debug("Lock '{}' is being obtained: {}", lockName, Thread.currentThread().getName());
            return ps.executeUpdate() >= 1;
        } finally {
            ps.close();
        }
    }

    private void lockViaInsert(Connection connection, String lockName, String sql) throws SQLException {
        getLogger().debug("Inserting new lock row for lock: '{}' being obtained by thread: {}", lockName, Thread.currentThread().getName());
        PreparedStatement ps = connection.prepareStatement(sql);
        try {
            ps.setString(1, lockName);
            ps.setObject(2, LocalDateTime.now());
            ps.setObject(3, LocalDateTime.now());
            ps.setString(4, "");
            if (ps.executeUpdate() != 1) {
                throw new SQLException("No row exists, and one could not be inserted in table crdf_lock for lock named: " + lockName);
            }
        } finally {
            ps.close();
        }
    }
}
