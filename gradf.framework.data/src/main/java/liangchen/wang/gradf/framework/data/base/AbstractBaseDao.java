package liangchen.wang.gradf.framework.data.base;


import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author LiangChen.Wang
 */
public abstract class AbstractBaseDao<E extends RootEntity, Q extends RootQuery> extends AbstractDao<E, Q> {
    private final Class<E> entityClass;
    private final Class<Q> queryClass;
    @Inject
    @Named("Crdf_Data_DaoBuilder")
    private DaoBuilder daoBuilder;

    @SuppressWarnings({"unchecked"})
    public AbstractBaseDao() {
        Class<?> implClass = getClass();
        Type thisType = implClass.getGenericSuperclass();
        Assert.INSTANCE.isTrue(thisType instanceof ParameterizedType, "必须设置参数化类型:<E extends RootEntity, Q extends RootQuery>");
        Type[] argTypes = ((ParameterizedType) thisType).getActualTypeArguments();
        entityClass = (Class<E>) argTypes[0];
        queryClass = (Class<Q>) argTypes[1];
    }

    @Override
    public boolean insert(E entity) {
        int rows = sqlSessionTemplate.insert(daoBuilder.insertId(entityClass), entity);
        if (rows == 1) {
            return true;
        }
        return false;
    }


    @Override
    public int deleteByQuery(Q query) {
        return sqlSessionTemplate.delete(daoBuilder.deleteByQueryId(queryClass), query);
    }

    @Override
    public int updateByQuery(E entity, Q query) {
        query.setEntity(entity);
        return sqlSessionTemplate.update(daoBuilder.updateByQueryId(entityClass, queryClass), query);
    }

    @Override
    public boolean exist(Q query) {
        int count = count(query);
        return count > 0;
    }

    @Override
    public int count(Q query) {
        return sqlSessionTemplate.selectOne(daoBuilder.countId(queryClass), query);
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
        if (CollectionUtil.INSTANCE.isEmpty(returnFields)) {
            query.setReturnFields(new String[]{"*"});
        } else {
            query.setReturnFields(returnFields);
        }
        List<E> list = sqlSessionTemplate.selectList(daoBuilder.listId(queryClass, entityClass), query);
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

}
