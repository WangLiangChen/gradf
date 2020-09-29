/**
 * Copyright 2009-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.javacrumbs.shedlock.provider.jdbctemplate;

import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider.Configuration;
import net.javacrumbs.shedlock.support.AbstractStorageAccessor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.TimeZone;

import static java.util.Objects.requireNonNull;

/**
 * Spring JdbcTemplate based implementation usable in JTA environment
 */
class JdbcTemplateStorageAccessor extends AbstractStorageAccessor {
    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final TimeZone timeZone;

    JdbcTemplateStorageAccessor(Configuration configuration) {
        this.jdbcTemplate = requireNonNull(configuration.getJdbcTemplate(), "jdbcTemplate can not be null");
        PlatformTransactionManager transactionManager = configuration.getTransactionManager() != null ?
                configuration.getTransactionManager() :
                new DataSourceTransactionManager(jdbcTemplate.getDataSource());

        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        this.timeZone = configuration.getTimeZone();
    }

    @Override
    public boolean insertRecord(LockConfiguration lockConfiguration) {
        String sql = "INSERT INTO gradf_lock (lock_key, lock_datetime, lock_until, lock_by) VALUES(?, ?, ?, ?)";
        Boolean result = transactionTemplate.execute(status -> {
            try {
                int insertedRows = jdbcTemplate.update(sql, preparedStatement -> {
                    preparedStatement.setString(1, lockConfiguration.getName());
                    setTimestamp(preparedStatement, 2, Instant.now());
                    setTimestamp(preparedStatement, 3, lockConfiguration.getLockAtMostUntil());
                    preparedStatement.setString(4, getHostname());
                });
                return insertedRows > 0;
            } catch (DuplicateKeyException e) {
                return false;
            } catch (DataIntegrityViolationException e) {
                logger.warn("Unexpected exception", e);
                return false;
            }
        });
        return result == null ? false : result;
    }

    @Override
    public boolean updateRecord(LockConfiguration lockConfiguration) {
        String sql = "UPDATE gradf_lock SET lock_datetime = ?, lock_until = ?, lock_by = ? WHERE lock_key = ? AND lock_until <= ?";
        Boolean result = transactionTemplate.execute(status -> {
            int updatedRows = jdbcTemplate.update(sql, statement -> {
                Instant now = Instant.now();
                setTimestamp(statement, 1, now);
                setTimestamp(statement, 2, lockConfiguration.getLockAtMostUntil());
                statement.setString(3, getHostname());
                statement.setString(4, lockConfiguration.getName());
                setTimestamp(statement, 5, now);
            });
            return updatedRows > 0;
        });
        return result == null ? false : result;
    }

    @Override
    public boolean extend(LockConfiguration lockConfiguration) {
        String sql = "UPDATE gradf_lock SET lock_until = ? WHERE lock_key = ? AND lock_by = ? AND lock_until > ? ";

        logger.debug("Extending lock={} until={}", lockConfiguration.getName(), lockConfiguration.getLockAtMostUntil());
        Boolean result = transactionTemplate.execute(status -> {
            int updatedRows = jdbcTemplate.update(sql, statement -> {
                setTimestamp(statement, 1, lockConfiguration.getLockAtMostUntil());
                statement.setString(2, lockConfiguration.getName());
                statement.setString(3, getHostname());
                setTimestamp(statement, 4, Instant.now());
            });
            return updatedRows > 0;
        });
        return result == null ? false : result;
    }

    private void setTimestamp(PreparedStatement preparedStatement, int parameterIndex, Instant time) throws SQLException {
        if (timeZone == null) {
            preparedStatement.setTimestamp(parameterIndex, Timestamp.from(time));
        } else {
            preparedStatement.setTimestamp(parameterIndex, Timestamp.from(time), Calendar.getInstance(timeZone));
        }
    }

    @Override
    public void unlock(LockConfiguration lockConfiguration) {
        String sql = "UPDATE gradf_lock SET lock_until = ? WHERE lock_key = ?";
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                jdbcTemplate.update(sql, statement -> {
                    setTimestamp(statement, 1, lockConfiguration.getUnlockTime());
                    statement.setString(2, lockConfiguration.getName());
                });
            }
        });
    }

}
