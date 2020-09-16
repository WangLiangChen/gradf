package liangchen.wang.gradf.framework.data.base;

import liangchen.wang.crdf.framework.commons.exeception.InfoException;
import liangchen.wang.crdf.framework.commons.pagination.PaginationResult;
import liangchen.wang.crdf.framework.commons.utils.CollectionUtil;
import liangchen.wang.crdf.framework.commons.validator.Assert;
import liangchen.wang.crdf.framework.commons.validator.AssertLevel;

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
public abstract class AbstractBaseDao<E extends RootEntity> extends AbstractDao<E> {
    private final Class<E> entityClass;
    @Inject
    @Named("Crdf_Data_DaoBuilder")
    private DaoBuilder daoBuilder;

    @SuppressWarnings({"unchecked"})
    public AbstractBaseDao() {
        Class<?> implClass = getClass();
        Type thisType = implClass.getGenericSuperclass();
        Assert.INSTANCE.isTrue(thisType instanceof ParameterizedType, AssertLevel.INFO, "必须设置参数化类型:<E extends RootEntity>");
        Type[] argTypes = ((ParameterizedType) thisType).getActualTypeArguments();
        entityClass = (Class<E>) argTypes[0];
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
    public int deleteByQuery(RootQuery query) {
        return sqlSessionTemplate.delete(daoBuilder.deleteByQueryId(query.getClass()), query);
    }

    @Override
    public int updateByQuery(E entity, RootQuery query) {
        query.setEntity(entity);
        return sqlSessionTemplate.update(daoBuilder.updateByQueryId(entityClass, query.getClass()), query);
    }

    @Override
    public boolean exist(RootQuery query) {
        int count = count(query);
        return count > 0;
    }

    @Override
    public int count(RootQuery query) {
        return sqlSessionTemplate.selectOne(daoBuilder.countId(query.getClass()), query);
    }

    @Override
    public E one(RootQuery query, String... returnFields) {
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
    public Optional<E> oneOptional(RootQuery query, String... returnFields) {
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
    public List<E> list(RootQuery query, String... returnFields) {
        if (CollectionUtil.INSTANCE.isEmpty(returnFields)) {
            query.setReturnFields(new String[]{"*"});
        } else {
            query.setReturnFields(returnFields);
        }
        List<E> list = sqlSessionTemplate.selectList(daoBuilder.listId(query.getClass(), entityClass), query);
        // 清除query的returnFields字段，以免影响缓存Key
        query.setReturnFields(null);
        return list;
    }

    @Override
    public PaginationResult<E> pagination(RootQuery query, String... returnFields) {
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
