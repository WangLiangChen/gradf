package liangchen.wang.gradf.framework.data.dao.impl;

import liangchen.wang.gradf.framework.data.condition.DataConditionAnnotation;
import liangchen.wang.gradf.framework.data.dao.AbstractDBLock;
import net.javacrumbs.shedlock.support.LockException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author LiangChen.Wang
 */
@DataConditionAnnotation
@Repository("Crdf_Data_ReplaceIntoLock")
public class ReplaceIntoLockImpl extends AbstractDBLock {
    private int maxRetry = 3;
    private long retryPeriod = 500L;

    private final String REPLACE_LOCK = "replace into crdf_lock values (?,?,?,?)";

    @Override
    protected void executeLockSQL(final Connection connection, final String lockKey) {
        SQLException lastException = null;
        for (int i = 0; i < maxRetry; i++) {
            try {
                if (lockViaReplaceInto(connection, lockKey, REPLACE_LOCK)) {
                    return;
                }
            } catch (SQLException e) {
                lastException = e;
                if ((i + 1) == maxRetry) {
                    getLogger().debug("Lock '{}' was not obtained by: {}", lockKey, Thread.currentThread().getName());
                } else {
                    getLogger().debug("Lock '{}' was not obtained by: {} - will try again.", lockKey, Thread.currentThread().getName());
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

    private boolean lockViaReplaceInto(Connection connection, String lockKey, String sql) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql);
        LocalDateTime now = LocalDateTime.now();
        try {
            ps.setString(1, lockKey);
            ps.setObject(2, now);
            ps.setObject(3, now);
            ps.setString(4, "");
            getLogger().debug("Lock '{}' is being obtained: {}", lockKey, Thread.currentThread().getName());
            return ps.executeUpdate() >= 1;
        } finally {
            ps.close();
        }
    }
}
