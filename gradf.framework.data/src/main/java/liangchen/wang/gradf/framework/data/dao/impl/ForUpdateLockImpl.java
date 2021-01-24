package liangchen.wang.gradf.framework.data.dao.impl;

import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.data.condition.JdbcConditionAnnotation;
import liangchen.wang.gradf.framework.data.dao.AbstractDBLock;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

;

/**
 * @author LiangChen.Wang
 */
@JdbcConditionAnnotation
@Repository("Gradf_Data_ForUpdateLock")
public class ForUpdateLockImpl extends AbstractDBLock {

    private final String SELECT_FOR_UPDATE_SQL = "select 0 from gradf_lock where lock_key=? for update";
    private final String INSERT_SQL = "insert into gradf_lock values(?,?,?,?)";


    @Override
    protected void executeLockSQL(final Connection connection, final String lockName) throws SQLException {
        if (!lockViaSelectForUpdate(connection, lockName)) {
            // 当数据不存在时，线程并不会等待，所有线程都会到达这里;
            // 当多个线程都到这里执行insert时，第一个进入的线程会等待(因为上面的for update)，其它线程会产生死锁异常，然后第一个进入线程会执行成功
            lockViaInsert(connection, lockName);
        }
    }

    private boolean lockViaSelectForUpdate(Connection connection, String lockName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(SELECT_FOR_UPDATE_SQL);
        ps.setString(1, lockName);
        getLogger().debug("Lock '{}' is being obtained:{} ", lockName, Thread.currentThread().getName());
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
            if (rs.next()) {
                // obtained lock, go
                getLogger().debug("lock row for lock: '{}' being obtained by thread:{} ", lockName, Thread.currentThread().getName());
                return true;
            }
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
        }
        return false;
    }

    private boolean lockViaInsert(Connection connection, String lockName) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(INSERT_SQL);
        ps.setString(1, lockName);
        LocalDateTime now = LocalDateTime.now();
        ps.setObject(2, now);
        ps.setObject(3, now);
        ps.setString(4, "");
        try {
            if (ps.executeUpdate() == 1) {
                // obtained lock, go
                getLogger().debug("Inserting new lock row for lock: '{}' being obtained by thread: {}", lockName, Thread.currentThread().getName());
                return true;
            }
            throw new InfoException("unknown error,updated rows is 0");
        } finally {
            closeStatement(ps);
        }
    }
}
