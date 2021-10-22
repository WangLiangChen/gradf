package liangchen.wang.gradf.framework.data.core;


import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.data.query.RootQuery;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ClassUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author LiangChen.Wang
 * 后续考虑拆分为Command和Query，已迁移优化
 */
public abstract class AbstractJdbcDao<E extends RootEntity, Q extends RootQuery> implements IDao<E, Q> {
    private final Class<E> entityClass;
    private final Class<Q> queryClass;
    @Inject
    protected JdbcTemplate jdbcTemplate;
    @Inject
    protected SqlSessionTemplate sqlSessionTemplate;
    @PersistenceContext
    protected EntityManager entityManager;

    @Inject
    private MybatisStatementBuilder mybatisStatementBuilder;

    @SuppressWarnings({"unchecked"})
    public AbstractJdbcDao() {
        Class<?> implClass = getClass();
        Type thisType = implClass.getGenericSuperclass();
        Assert.INSTANCE.isTrue(thisType instanceof ParameterizedType, "必须设置参数化类型:<E extends RootEntity, Q extends RootQuery>");
        Type[] argTypes = ((ParameterizedType) thisType).getActualTypeArguments();
        entityClass = (Class<E>) argTypes[0];
        queryClass = (Class<Q>) argTypes[1];
    }

    @Override
    public boolean insert(E entity) {
        if (null == entity) {
            return false;
        }
        int rows = sqlSessionTemplate.insert(mybatisStatementBuilder.insertId(entityClass), entity);
        if (rows == 1) {
            return true;
        }
        return false;
    }

    @Override
    public int insertBatch(List<E> entities) {
        if (null == entities || entities.size() == 0) {
            return 0;
        }
        int rows = sqlSessionTemplate.insert(mybatisStatementBuilder.insertBatchId(entityClass), entities);
        return rows;
    }


    @Override
    public int deleteByQuery(Q query) {
        if (null == query) {
            return 0;
        }
        return sqlSessionTemplate.delete(mybatisStatementBuilder.deleteByQueryId(queryClass), query);
    }

    @Override
    public int updateByQuery(E entity, Q query) {
        if (null == entity || null == query) {
            return 0;
        }
        query.setEntity(entity);
        return sqlSessionTemplate.update(mybatisStatementBuilder.updateByQueryId(entityClass, queryClass), query);
    }

    @Override
    public boolean exist(Q query) {
        int count = count(query);
        return count > 0;
    }

    @Override
    public int count(Q query) {
        if (null == query) {
            return 0;
        }
        return sqlSessionTemplate.selectOne(mybatisStatementBuilder.countId(queryClass), query);
    }

    @Override
    public E one(Q query, String... returnFields) {
        List<E> list = list(query, returnFields);
        int size = list.size();
        switch (size) {
            case 0:
                return null;
            case 1:
                return list.get(0);
            default:
                throw new InfoException("Expected one result (or null) to be returned by selectOne(), but found: {}", size);
        }
    }

    @Override
    public Optional<E> oneOptional(Q query, String... returnFields) {
        List<E> list = list(query, returnFields);
        int size = list.size();
        switch (size) {
            case 0:
                return Optional.empty();
            case 1:
                return Optional.of(list.get(0));
            default:
                throw new InfoException("Expected one result (or null) to be returned by selectOne(), but found: {}", size);
        }
    }

    @Override
    public List<E> list(Q query, String... returnFields) {
        if (null == query) {
            return Collections.emptyList();
        }
        if (CollectionUtil.INSTANCE.isEmpty(returnFields)) {
            query.setReturnFields(new String[]{"*"});
        } else {
            query.setReturnFields(returnFields);
        }
        List<E> list = sqlSessionTemplate.selectList(mybatisStatementBuilder.listId(queryClass, entityClass), query);
        // 清除query的returnFields字段，以免影响缓存Key
        query.setReturnFields(null);
        return list;
    }

    @Override
    public PaginationResult<E> pagination(Q query, String... returnFields) {
        int count = count(query);
        PaginationResult<E> paginationResult = PaginationResult.newInstance();
        paginationResult.setTotalRecords(count);
        paginationResult.setPage(query.getPage());
        paginationResult.setRows(query.getRows());
        if (count > 0) {
            List<E> datas = list(query, returnFields);
            paginationResult.setDatas(datas);
        } else {
            paginationResult.setDatas(Collections.emptyList());
        }
        return paginationResult;
    }

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
        // queryForObject throw a DataAccessException while the ResultSet is empty
        try {
            if (ClassUtils.isPrimitiveOrWrapper(clazz) || String.class == clazz) {
                return jdbcTemplate.queryForObject(sqlBuilder.getSql(), clazz, sqlBuilder.getArgs());
            }
            return jdbcTemplate.queryForObject(sqlBuilder.getSql(), BeanPropertyRowMapper.newInstance(clazz), sqlBuilder.getArgs());
        } catch (DataAccessException e) {
            return null;
        }
    }

    protected Map<String, Object> queryForMap(SqlBuilder sqlBuilder) {
        return jdbcTemplate.queryForMap(sqlBuilder.getSql(), sqlBuilder.getArgs());
    }

    protected <E> List<E> queryForList(SqlBuilder sqlBuilder, Class<E> clazz) {
        if (ClassUtils.isPrimitiveOrWrapper(clazz) || String.class == clazz) {
            return jdbcTemplate.queryForList(sqlBuilder.getSql(), clazz, sqlBuilder.getArgs());
        }
        return jdbcTemplate.query(sqlBuilder.getSql(), BeanPropertyRowMapper.newInstance(clazz), sqlBuilder.getArgs());
    }


    protected List<Map<String, Object>> queryForList(SqlBuilder sqlBuilder) {
        return jdbcTemplate.queryForList(sqlBuilder.getSql(), sqlBuilder.getArgs());
    }


    protected ResultSetMetaData queryForMetaData(SqlBuilder sqlBuilder) {
        return jdbcTemplate.query(sqlBuilder.getSql(), ResultSet::getMetaData);
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public Class<Q> getQueryClass() {
        return queryClass;
    }
}
