package liangchen.wang.gradf.framework.data.dao.impl;

import liangchen.wang.gradf.framework.data.condition.DataConditionAnnotation;
import liangchen.wang.gradf.framework.data.dao.ISequenceDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author LiangChen.Wang
 */
@DataConditionAnnotation
@Repository("Gradf_Data_SequenceDao")
public class SequenceDaoImpl implements ISequenceDao {
    private static final String SELECT_SQL = "select sequence_number from gradf_sequence where sequence_key=?";
    private static final String INSERT_SQL = "insert into gradf_sequence values(?,?) on duplicate key update sequence_number=sequence_number+1";
    private final JdbcTemplate jdbcTemplate;

    @Inject
    public SequenceDaoImpl(@Named("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Long sequenceNumber(String sequenceKey, int initialValue) {
        jdbcTemplate.update(INSERT_SQL, sequenceKey, initialValue);
        Long sequenceNumber = jdbcTemplate.queryForObject(SELECT_SQL, new Object[]{sequenceKey}, Long.class);
        return sequenceNumber;
    }
}
