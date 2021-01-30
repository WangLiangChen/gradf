package liangchen.wang.gradf.framework.data.dao.impl;

import liangchen.wang.gradf.framework.data.condition.JdbcConditionAnnotation;
import liangchen.wang.gradf.framework.data.dao.ISequenceDao;
import liangchen.wang.gradf.framework.data.utils.TransactionUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;

import javax.inject.Inject;

/**
 * @author LiangChen.Wang
 */
@JdbcConditionAnnotation
@Repository("Gradf_Data_SequenceDao")
public class SequenceDaoImpl implements ISequenceDao {
    private final String SELECT_SQL = "select sequence_number from gradf_sequence where sequence_key=? for update";
    private final String UPDATE_SQL = "update gradf_sequence set sequence_number=sequence_number+1 where sequence_key=?";
    private final String INSERT_SQL = "insert into gradf_sequence values(?,?)";
    private final JdbcTemplate jdbcTemplate;

    @Inject
    public SequenceDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Long sequenceNumber(String sequenceKey, long initialValue) {
        return TransactionUtil.INSTANCE.execute(() -> {
            Long sequenceNumber = jdbcTemplate.queryForObject(SELECT_SQL, Long.class, new Object[]{sequenceKey});
            if (null == sequenceNumber) {
                jdbcTemplate.update(INSERT_SQL, new Object[]{sequenceKey, initialValue});
                return sequenceNumber;
            }
            jdbcTemplate.update(UPDATE_SQL, new Object[]{sequenceKey});
            return sequenceNumber;
        }, Isolation.REPEATABLE_READ);
    }
}
