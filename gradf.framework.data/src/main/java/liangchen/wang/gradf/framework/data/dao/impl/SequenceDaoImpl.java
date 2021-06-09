package liangchen.wang.gradf.framework.data.dao.impl;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.data.condition.JdbcConditionAnnotation;
import liangchen.wang.gradf.framework.data.dao.ISequenceDao;
import liangchen.wang.gradf.framework.data.enumeration.DbConnectionsManager;
import org.springframework.stereotype.Repository;

import java.sql.*;

/**
 * @author LiangChen.Wang
 */
@JdbcConditionAnnotation
@Repository("Gradf_Data_SequenceDao")
public class SequenceDaoImpl implements ISequenceDao {
    private final String SELECT_SQL = "select sequence_number from gradf_sequence where sequence_key=?";
    private final String UPDATE_SQL = "update gradf_sequence set sequence_number=sequence_number+1 where sequence_key=?";
    private final String INSERT_SQL = "insert into gradf_sequence values(?,?)";

    @Override
    public Long sequenceNumber(String sequenceKey, long initialValue) {
        for (int i = 0; i < 3; i++) {
            long sequenceNumber = fetchSequenceNumber(sequenceKey, initialValue);
            if (-1 == sequenceNumber) {
                continue;
            }
            return sequenceNumber;
        }
        throw new InfoException("Could't fetch sequence number!");
    }

    private long fetchSequenceNumber(String sequenceKey, long initialValue) {
        return DbConnectionsManager.INSTANCE.executeInNonManagedConnection((connection) -> {
            try {
                // 先更新
                PreparedStatement statement = connection.prepareStatement(UPDATE_SQL);
                statement.setString(1, sequenceKey);
                int rows = statement.executeUpdate();
                statement.close();
                // 更新成功,说明数据存在，则查询返回
                if (1 == rows) {
                    statement = connection.prepareStatement(SELECT_SQL);
                    statement.setString(1, sequenceKey);
                    ResultSet resultSet = statement.executeQuery();
                    Long sequenceNumber = null;
                    if (resultSet.next()) {
                        sequenceNumber = resultSet.getLong("sequence_number");
                    }
                    resultSet.close();
                    statement.close();
                    return sequenceNumber;
                }
                // 更新失败则尝试插入
                try {
                    statement = connection.prepareStatement(INSERT_SQL);
                    statement.setString(1, sequenceKey);
                    statement.setLong(2, initialValue);
                    statement.executeUpdate();
                    statement.close();
                    return initialValue;
                } catch (SQLIntegrityConstraintViolationException ex) {
                    // 主键冲突 插入失败
                    return -1L;
                }
            } catch (SQLException e) {
                throw new ErrorException(e);
            }
        }, Connection.TRANSACTION_READ_COMMITTED);
    }


}
