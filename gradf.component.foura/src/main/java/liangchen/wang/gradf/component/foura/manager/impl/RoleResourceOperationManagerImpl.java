package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.component.foura.dao.IRoleResourceOperationDao;
import liangchen.wang.gradf.component.foura.dao.entity.RoleResourceOperation;
import liangchen.wang.gradf.component.foura.dao.query.RoleResourceOperationQuery;
import liangchen.wang.gradf.component.foura.manager.IRoleResourceOperationManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.RoleResourceOperationParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleResourceOperationResultDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.utils.ContextUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.enumeration.DataMode;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LiangChen.Wang 2020-04-12 23:55:49
 */
@Component("Gradf_Foura_DefaultRoleResourceOperationManager")
public class RoleResourceOperationManagerImpl extends AbstractManager<RoleResourceOperation, RoleResourceOperationQuery, RoleResourceOperationResultDomain> implements IRoleResourceOperationManager {
    @Inject
    public RoleResourceOperationManagerImpl(@Named("Gradf_Foura_DefaultRoleResourceOperationDao") IRoleResourceOperationDao dao) {
        super("角色/资源/操作", "RoleResourceOperation", dao);
    }

    @Override
    public boolean insert(RoleResourceOperationParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        final Long operator = ContextUtil.INSTANCE.getOperator();
        parameter.populateEntity((entity) -> {
            RoleResourceOperation roleResourceOperation = ClassBeanUtil.INSTANCE.cast(entity);
            //TODO 这里可以调整Entity，比如设置主键/状态等
            Assert.INSTANCE.notBlankElseRun(roleResourceOperation.getStatus(), () -> roleResourceOperation.setStatus(Status.NORMAL.name()));
            Assert.INSTANCE.notNullElseRun(roleResourceOperation.getData_mode(), () -> roleResourceOperation.setData_mode(DataMode.A.getValue()));
            roleResourceOperation.setCreator(operator);
            roleResourceOperation.setModifier(operator);
            roleResourceOperation.initFields();
        });
        return super.insert(parameter);
    }

    @Override
    public boolean deleteByPrimaryKey(Long role_id, Long resource_id, Long operation_id) {
        return updateStatusByPrimaryKey(role_id, resource_id, operation_id, Status.DELETED.name());
    }

    @Override
    public boolean updateByPrimaryKey(RoleResourceOperationParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        RoleResourceOperationQuery query = RoleResourceOperationQuery.newInstance();
        query.setRole_id(parameter.getRole_id());
        query.setResource_id(parameter.getResource_id());
        query.setOperation_id(parameter.getOperation_id());
        int rows = updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(RoleResourceOperationParameterDomain parameter, RoleResourceOperationQuery query) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        parameter.populateEntity((entity) -> {
            RoleResourceOperation roleResourceOperation = ClassBeanUtil.INSTANCE.cast(entity);
            // TODO 这里添加不更新或者不论是否空值总更新的字段
            roleResourceOperation.setModify_datetime(LocalDateTime.now());
            roleResourceOperation.setModifier(ContextUtil.INSTANCE.getOperator());
        });
        return super.updateByQuery(parameter, query);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long role_id, Long resource_id, Long operation_id, String statusTo, String... statusFrom) {
        return updateStatusByPrimaryKey(role_id, resource_id, operation_id, statusTo, statusFrom, null);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long role_id, Long resource_id, Long operation_id, String statusTo, String[] statusIn, String[] statusNotIn) {
        Assert.INSTANCE.notNull(role_id, "参数不能为空");
        Assert.INSTANCE.notNull(resource_id, "参数不能为空");
        Assert.INSTANCE.notNull(operation_id, "参数不能为空");
        Assert.INSTANCE.notBlank(statusTo, "状态不能为空");
        RoleResourceOperationParameterDomain parameter = RoleResourceOperationParameterDomain.newInstance();
        parameter.setStatus(statusTo);
        RoleResourceOperationQuery query = RoleResourceOperationQuery.newInstance();
        query.setRole_id(role_id);
        query.setResource_id(resource_id);
        query.setOperation_id(operation_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        int rows = super.updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public RoleResourceOperationResultDomain byPrimaryKey(Long role_id, Long resource_id, Long operation_id, String... returnFields) {
        return byPrimaryKey(role_id, resource_id, operation_id, null, null, returnFields);
    }

    @Override
    public RoleResourceOperationResultDomain byPrimaryKeyOrThrow(Long role_id, Long resource_id, Long operation_id, String... returnFields) {
        return byPrimaryKeyOrThrow(role_id, resource_id, operation_id, null, null, returnFields);
    }

    @Override
    public RoleResourceOperationResultDomain byPrimaryKeyOrThrow(Long role_id, Long resource_id, Long operation_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        RoleResourceOperationResultDomain resultDomain = byPrimaryKey(role_id, resource_id, operation_id, statusIn, statusNotIn, returnFields);
        Assert.INSTANCE.notNull(resultDomain, "数据不存在或者状态错误");
        return resultDomain;
    }

    @Override
    public RoleResourceOperationResultDomain byPrimaryKey(Long role_id, Long resource_id, Long operation_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        Assert.INSTANCE.notNull(role_id, "参数不能为空");
        Assert.INSTANCE.notNull(resource_id, "参数不能为空");
        Assert.INSTANCE.notNull(operation_id, "参数不能为空");
        RoleResourceOperationQuery query = RoleResourceOperationQuery.newInstance();
        query.setRole_id(role_id);
        query.setResource_id(resource_id);
        query.setOperation_id(operation_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        return one(query, returnFields);
    }

    @Override
    public List<RoleResourceOperationResultDomain> list(RoleResourceOperationQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<RoleResourceOperationResultDomain> pagination(RoleResourceOperationQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

}
