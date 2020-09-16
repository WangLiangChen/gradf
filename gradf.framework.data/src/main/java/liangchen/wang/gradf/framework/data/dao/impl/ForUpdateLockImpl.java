package liangchen.wang.gradf.framework.data.dao.impl;

import liangchen.wang.crdf.framework.commons.exeception.InfoException;
import liangchen.wang.crdf.framework.data.condition.DataConditionAnnotation;
import liangchen.wang.crdf.framework.data.dao.AbstractDBLock;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@DataConditionAnnotation
@Repository("Crdf_Data_ForUpdateLock")
public class ForUpdateLockImpl extends AbstractDBLock {
    private int maxRetry = 3;
    private long retryPeriod = 500L;

    private final String SELECT_FOR_LOCK = "select 0 from crdf_lock where lock_key=? for update";
    private final String INSERT_LOCK = "insert into crdf_lock values(?,?,?,?)";


    @Override
    protected void executeLockSQL(final Connection connection, final String lockName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        SQLException initCause = null;
        // attempt lock two times (to work-around possible race conditions in inserting the lock row the first time running)
        int count = 0;
        do {
            count++;
            try {
                ps = connection.prepareStatement(SELECT_FOR_LOCK);
                ps.setString(1, lockName);
                getLogger().debug("Lock '{}' is being obtained:{} ", lockName, Thread.currentThread().getName());
                rs = ps.executeQuery();
                if (rs.next()) {
                    // obtained lock, go
                    getLogger().debug("lock row for lock: '{}' being obtained by thread:{} ", lockName, Thread.currentThread().getName());
                    return;
                }
                rs.close();
                ps.close();
                // 当数据不存在时，线程并不会等待，所有线程都会到达这里;
                // 当多个线程都到这里执行insert时，第一个进入的线程会等待，其它线程会产生死锁异常，然后第一个进入线程会执行成功
                ps = connection.prepareStatement(INSERT_LOCK);
                ps.setString(1, lockName);
                LocalDateTime now = LocalDateTime.now();
                ps.setObject(2, now);
                ps.setObject(3, now);
                ps.setString(4, "");
                int rows = ps.executeUpdate();
                if (rows == 1) {
                    // obtained lock, go
                    getLogger().debug("Inserting new lock row for lock: '{}' being obtained by thread: {}", lockName, Thread.currentThread().getName());
                    return;
                }
                // insert返回0行的场景，mysql未捕捉到，其它db可能会有
                if (count < maxRetry) {
                    // pause a bit to give another thread some time to commit the insert of the new lock row
                    try {
                        Thread.sleep(retryPeriod);
                    } catch (InterruptedException ignore) {
                        Thread.currentThread().interrupt();
                    }
                    // try again ...
                    continue;
                }
                throw new SQLException("No row exists, and one could not be inserted in table crdf_lock for lock named: " + lockName);
            } catch (SQLException sqle) {
                if (initCause == null) {
                    initCause = sqle;
                }
                getLogger().debug("Lock '{}' was not obtained by: {}, cause:{}", lockName, Thread.currentThread().getName(), sqle.getMessage());
                if (count < maxRetry) {
                    getLogger().debug("{}- will try again.", Thread.currentThread().getName());
                    // pause a bit to give another thread some time to commit the insert of the new lock row
                    try {
                        Thread.sleep(retryPeriod);
                    } catch (InterruptedException ignore) {
                        Thread.currentThread().interrupt();
                    }
                    // try again ...
                    continue;
                }
                throw new InfoException("Failure obtaining db row lock: " + sqle.getMessage(), sqle);
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception ignore) {
                        getLogger().error("close resultset error", ignore);
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception ignore) {
                        getLogger().error("close statement error", ignore);
                    }
                }
            }
        } while (count < (maxRetry + 1));
        throw new InfoException("Failure obtaining db row lock, reached maximum number of attempts. Initial exception (if any) attached as root cause.", initCause);
    }
}
