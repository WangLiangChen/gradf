package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.component.foura.dao.IOperationDao;
import liangchen.wang.gradf.component.foura.dao.entity.Operation;
import liangchen.wang.gradf.component.foura.dao.query.OperationQuery;
import liangchen.wang.gradf.component.foura.manager.IOperationManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.OperationParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.OperationResultDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.utils.ContextUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.data.utils.UidDb;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author LiangChen.Wang 2020-04-12 23:47:02
 */
@Component("Gradf_Foura_DefaultOperationManager")
public class OperationManagerImpl extends AbstractManager<Operation, OperationQuery, OperationResultDomain> implements IOperationManager {
    @Inject
    public OperationManagerImpl(@Named("Gradf_Foura_DefaultOperationDao") IOperationDao dao) {
        super("操作", "Operation", dao);
    }

    @Override
    public boolean insert(OperationParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        final Long operator = ContextUtil.INSTANCE.getOperator();
        parameter.populateEntity((entity) -> {
            Operation operation = ClassBeanUtil.INSTANCE.cast(entity);
            Assert.INSTANCE.notNullElseRun(operation.getOperation_id(), () -> operation.setOperation_id(UidDb.INSTANCE.uid()));
            operation.initFields();
        });
        return super.insert(parameter);
    }

    @Override
    public boolean deleteByPrimaryKey(Long operation_id) {
        Assert.INSTANCE.notNull(operation_id, "operation_id不能为空");
        OperationQuery query = OperationQuery.newInstance();
        query.setOperation_id(operation_id);
        return super.deleteByQuery(query) == 1;
    }

    @Override
    public boolean updateByPrimaryKey(OperationParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        OperationQuery query = OperationQuery.newInstance();
        query.setOperation_id(parameter.getOperation_id());
        int rows = updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(OperationParameterDomain parameter, OperationQuery query) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        parameter.populateEntity((entity) -> {
        });
        return super.updateByQuery(parameter, query);
    }


    @Override
    public OperationResultDomain byPrimaryKey(Long operation_id, String... returnFields) {
        Assert.INSTANCE.notNull(operation_id, "参数不能为空");
        OperationQuery query = OperationQuery.newInstance();
        query.setOperation_id(operation_id);
        return one(query, returnFields);
    }

    @Override
    public OperationResultDomain byPrimaryKeyOrThrow(Long operation_id, String... returnFields) {
        OperationResultDomain resultDomain = byPrimaryKey(operation_id, returnFields);
        Assert.INSTANCE.notNull(resultDomain, "数据不存在");
        return resultDomain;
    }


    @Override
    public List<OperationResultDomain> list(OperationQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<OperationResultDomain> pagination(OperationQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

}
