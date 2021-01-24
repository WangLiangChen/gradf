package liangchen.wang.gradf.component.commons.base;

import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.commons.validator.InsertGroup;
import liangchen.wang.gradf.framework.commons.validator.UpdateGroup;
import liangchen.wang.gradf.framework.data.core.IDao;
import liangchen.wang.gradf.framework.data.core.RootEntity;
import liangchen.wang.gradf.framework.data.core.RootQuery;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author LiangChen.Wang 2019/11/21 9:52
 */
public abstract class AbstractManager<E extends RootEntity, Q extends RootQuery, R extends ResultDomain> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final String business_name;
    private final String business_type;
    private final IDao<E, Q> dao;
    private final Class<E> entityClass;
    private final Class<R> resultDomainClass;

    @SuppressWarnings({"unchecked"})
    public AbstractManager(String business_name, String business_type, IDao<E, Q> dao) {
        this.business_name = business_name;
        this.business_type = business_type;
        this.dao = dao;
        Class<?> implClass = getClass();
        Type thisType = implClass.getGenericSuperclass();
        Assert.INSTANCE.isTrue(thisType instanceof ParameterizedType, "必须设置泛型参数:<E extends RootEntity, Q extends RootQuery, R extends ResultDomain>");
        Type[] argTypes = ((ParameterizedType) thisType).getActualTypeArguments();
        Assert.INSTANCE.isTrue(argTypes.length > 2, "必须设置泛型参数:<E extends RootEntity, Q extends RootQuery, R extends ResultDomain>");
        entityClass = (Class<E>) argTypes[0];
        resultDomainClass = (Class<R>) argTypes[1];
    }

    protected boolean insert(ParameterDomain<E> parameter) {
        Assert.INSTANCE.validate(parameter, InsertGroup.class);
        E entity = parameter.copyTo(entityClass);
        entity.initOperator();
        entity.initFields();
        Consumer<E> consumer = parameter.getEntityConsumer();
        if (consumer != null) {
            consumer.accept(entity);
        }
        return dao.insert(entity);
    }

    protected int deleteByQuery(Q query) {
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        return dao.deleteByQuery(query);
    }

    protected int updateByQuery(ParameterDomain<E> parameter, Q query) {
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        Assert.INSTANCE.validate(parameter, UpdateGroup.class);
        E entity = parameter.copyTo(entityClass);
        Consumer<E> consumer = parameter.getEntityConsumer();
        if (null != consumer) {
            consumer.accept(entity);
        }
        return dao.updateByQuery(entity, query);
    }

    protected boolean exist(Q query) {
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        return dao.exist(query);
    }

    protected int count(Q query) {
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        return dao.count(query);
    }

    protected R one(Q query, String... returnFields) {
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        E entity = dao.one(query, returnFields);
        if (null == entity) {
            return null;
        }
        return entity.copyTo(resultDomainClass);
    }

    protected R one(Q query, Consumer<R> consumer, String... returnFields) {
        Assert.INSTANCE.notNull(query, "参数不能为空");
        E entity = dao.one(query, returnFields);
        if (null == entity) {
            return null;
        }
        return entity.copyTo(resultDomainClass, consumer);
    }

    protected List<R> list(Q query, String... returnFields) {
        Assert.INSTANCE.notNull(query, "参数不能为空");
        List<E> entities = dao.list(query, returnFields);
        return CollectionUtil.INSTANCE.copyList(entities, resultDomainClass);
    }

    protected List<R> list(Q query, Consumer<R> consumer, String... returnFields) {
        Assert.INSTANCE.notNull(query, "参数不能为空");
        List<E> entities = dao.list(query, returnFields);
        return CollectionUtil.INSTANCE.copyList(entities, resultDomainClass, consumer);
    }

    protected PaginationResult<R> pagination(Q query, String... returnFields) {
        Assert.INSTANCE.notNull(query, "参数不能为空");
        PaginationResult<E> paginationEntity = dao.pagination(query, returnFields);
        return paginationEntity.copyTo(resultDomainClass);
    }

    protected PaginationResult<R> pagination(Q query, Consumer<R> consumer, String... returnFields) {
        Assert.INSTANCE.notNull(query, "参数不能为空");
        PaginationResult<E> paginationEntity = dao.pagination(query, returnFields);
        return paginationEntity.copyTo(resultDomainClass, consumer);
    }

    @SuppressWarnings("unchecked")
    protected <T> T getDao() {
        return (T) dao;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public String getBusiness_type() {
        return business_type;
    }
}
