package liangchen.wang.gradf.framework.data.dao.impl;

import liangchen.wang.gradf.framework.data.condition.JdbcConditionAnnotation;
import liangchen.wang.gradf.framework.data.dao.AbstractDBLock;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author LiangChen.Wang
 */
@JdbcConditionAnnotation
@Repository("Gradf_Data_ReplaceIntoLock")
public class ReplaceIntoLockImpl extends AbstractDBLock {

    private final String REPLACE_LOCK = "replace into gradf_lock values (?,?,?,?)";

    @Override
    protected void executeLockSQL(final Connection connection, final String lockKey) throws SQLException {
        lockViaReplaceInto(connection, lockKey, REPLACE_LOCK);
    }

    private boolean lockViaReplaceInto(Connection connection, String lockKey, String sql) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql);
        LocalDateTime now = LocalDateTime.now();
        ps.setString(1, lockKey);
        ps.setObject(2, now);
        ps.setObject(3, now);
        ps.setString(4, "");
        try {
            getLogger().debug("Lock '{}' is being obtained: {}", lockKey, Thread.currentThread().getName());
            return ps.executeUpdate() >= 1;
        } finally {
            closeStatement(ps);
        }
    }
}
