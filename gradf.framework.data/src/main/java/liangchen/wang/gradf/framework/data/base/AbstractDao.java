package liangchen.wang.gradf.framework.data.base;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Map;

/**
 * @author LiangChen.Wang 2019/11/12 15:15
 */
public abstract class AbstractDao<E extends RootEntity, Q extends RootQuery> implements IAbstractDao<E, Q> {
    @Resource(name = "jdbcTemplate")
    protected JdbcTemplate jdbcTemplate;
    @Resource(name = "sqlSessionTemplate")
    protected SqlSessionTemplate sqlSessionTemplate;
    @PersistenceContext
    protected EntityManager entityManager;

    protected <I> I getMapper(Class<I> type) {
        return this.sqlSessionTemplate.getMapper(type);
    }

    protected int executeSql(SqlBuilder sqlBuilder) {
        return jdbcTemplate.update(sqlBuilder.getSql(), sqlBuilder.getArgs());
    }

    protected int[] executeBatchSql(SqlBuilder sqlBuilder) {
        return jdbcTemplate.batchUpdate(sqlBuilder.getSql(), sqlBuilder.getBatchArgs());
    }

    protected <E> E queryForObject(Class<E> clazz, SqlBuilder sqlBuilder) {
        try {
            if (ClassUtils.isPrimitiveOrWrapper(clazz) || String.class == clazz) {
                return jdbcTemplate.queryForObject(sqlBuilder.getSql(), sqlBuilder.getArgs(), clazz);
            }
            return jdbcTemplate.queryForObject(sqlBuilder.getSql(), sqlBuilder.getArgs(), BeanPropertyRowMapper.newInstance(clazz));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    protected Map<String, Object> queryForMap(SqlBuilder sqlBuilder) {
        Map<String, Object> map;
        try {
            map = jdbcTemplate.queryForMap(sqlBuilder.getSql(), sqlBuilder.getArgs());
        } catch (EmptyResultDataAccessException e) {
            map = null;
        }
        return map;
    }

    protected <E> List<E> queryForList(SqlBuilder sqlBuilder, Class<E> clazz) {
        if (ClassUtils.isPrimitiveOrWrapper(clazz) || String.class == clazz) {
            return jdbcTemplate.queryForList(sqlBuilder.getSql(), sqlBuilder.getArgs(), clazz);
        }
        return jdbcTemplate.query(sqlBuilder.getSql(), sqlBuilder.getArgs(), BeanPropertyRowMapper.newInstance(clazz));
    }


    protected List<Map<String, Object>> queryForList(SqlBuilder sqlBuilder) {
        return jdbcTemplate.queryForList(sqlBuilder.getSql(), sqlBuilder.getArgs());
    }


    protected ResultSetMetaData queryForMetaData(SqlBuilder sqlBuilder) {
        return jdbcTemplate.query(sqlBuilder.getSql(), ResultSet::getMetaData);
    }
}
