package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.business.base.AbstractManager;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.commons.utils.ContextUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.commons.validator.AssertLevel;
import liangchen.wang.gradf.framework.data.enumeration.DataMode;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.component.foura.dao.IRoleResourcePrivilegeDao;
import liangchen.wang.gradf.component.foura.dao.entity.RoleResourcePrivilege;
import liangchen.wang.gradf.component.foura.dao.query.RoleResourcePrivilegeQuery;
import liangchen.wang.gradf.component.foura.manager.IRoleResourcePrivilegeManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.RoleResourcePrivilegeParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleResourcePrivilegeResultDomain;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LiangChen.Wang 2020-04-12 23:55:23
*/
@Component("Crdf_Foura_DefaultRoleResourcePrivilegeManager")
public class RoleResourcePrivilegeManagerImpl extends AbstractManager<RoleResourcePrivilege, RoleResourcePrivilegeResultDomain> implements IRoleResourcePrivilegeManager {
    @Inject
    public RoleResourcePrivilegeManagerImpl(@Named("Crdf_Foura_DefaultRoleResourcePrivilegeDao") IRoleResourcePrivilegeDao dao) {
        super("角色/资源/权限", "RoleResourcePrivilege", dao);
    }

    @Override
    public boolean insert(RoleResourcePrivilegeParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, AssertLevel.INFO, "参数不能为空");
        final Long operator = ContextUtil.INSTANCE.getOperator();
        parameter.populateEntity((entity) -> {
            RoleResourcePrivilege roleResourcePrivilege = ClassBeanUtil.INSTANCE.classCast(entity);
            //TODO 这里可以调整Entity，比如设置主键/状态等
            Assert.INSTANCE.notBlank(roleResourcePrivilege.getStatus(), () -> roleResourcePrivilege.setStatus(Status.NORMAL.name()));
            Assert.INSTANCE.notNull(roleResourcePrivilege.getData_mode(), () -> roleResourcePrivilege.setData_mode(DataMode.A.getValue()));
            roleResourcePrivilege.setCreator(operator);
            roleResourcePrivilege.setModifier(operator);
            roleResourcePrivilege.initFields();
        });
        return super.insert(parameter);
    }

    @Override
    public boolean deleteByPrimaryKey(Long role_id, Long resource_id) {
        return updateStatusByPrimaryKey(role_id, resource_id, Status.DELETED.name());
    }

    @Override
    public boolean updateByPrimaryKey(RoleResourcePrivilegeParameterDomain parameter) {
         Assert.INSTANCE.notNull(parameter, AssertLevel.INFO, "参数不能为空");
        RoleResourcePrivilegeQuery query = RoleResourcePrivilegeQuery.newInstance();
        query.setRole_id(parameter.getRole_id());
        query.setResource_id(parameter.getResource_id());
        int rows =  updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(RoleResourcePrivilegeParameterDomain parameter, RoleResourcePrivilegeQuery query) {
        Assert.INSTANCE.notNull(parameter, AssertLevel.INFO, "参数不能为空");
        Assert.INSTANCE.notNull(query, AssertLevel.INFO, "查询参数不能为空");
        parameter.populateEntity((entity) -> {
            RoleResourcePrivilege roleResourcePrivilege = ClassBeanUtil.INSTANCE.classCast(entity);
            // TODO 这里添加不更新或者不论是否空值总更新的字段
            roleResourcePrivilege.setModify_datetime(LocalDateTime.now());
            roleResourcePrivilege.setModifier(ContextUtil.INSTANCE.getOperator());
        });
        return super.updateByQuery(parameter, query);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long role_id, Long resource_id, String statusTo, String... statusFrom) {
        return updateStatusByPrimaryKey(role_id, resource_id, statusTo, statusFrom, null);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long role_id, Long resource_id, String statusTo, String[] statusIn, String[] statusNotIn) {
        Assert.INSTANCE.notBlank(role_id, AssertLevel.INFO, "参数不能为空");
        Assert.INSTANCE.notBlank( resource_id, AssertLevel.INFO, "参数不能为空");
        Assert.INSTANCE.notBlank(statusTo, AssertLevel.INFO, "状态不能为空");
        RoleResourcePrivilegeParameterDomain parameter = RoleResourcePrivilegeParameterDomain.newInstance();
        parameter.setStatus(statusTo);
        RoleResourcePrivilegeQuery query = RoleResourcePrivilegeQuery.newInstance();
        query.setRole_id(role_id);
        query.setResource_id(resource_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        int rows = super.updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public RoleResourcePrivilegeResultDomain byPrimaryKey(Long role_id, Long resource_id, String... returnFields) {
        return byPrimaryKey(role_id, resource_id, null, null, returnFields);
    }

    @Override
    public RoleResourcePrivilegeResultDomain byPrimaryKeyOrThrow(Long role_id, Long resource_id, String... returnFields) {
        return byPrimaryKeyOrThrow(role_id, resource_id, null, null, returnFields);
    }

    @Override
    public RoleResourcePrivilegeResultDomain byPrimaryKeyOrThrow(Long role_id, Long resource_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        RoleResourcePrivilegeResultDomain resultDomain = byPrimaryKey(role_id, resource_id, statusIn, statusNotIn, returnFields);
        Assert.INSTANCE.notNull(resultDomain, AssertLevel.INFO, "数据不存在或者状态错误");
        return resultDomain;
    }

    @Override
    public RoleResourcePrivilegeResultDomain byPrimaryKey(Long role_id, Long resource_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        Assert.INSTANCE.notBlank(role_id, AssertLevel.INFO, "参数不能为空");
        Assert.INSTANCE.notBlank( resource_id, AssertLevel.INFO, "参数不能为空");
        RoleResourcePrivilegeQuery query = RoleResourcePrivilegeQuery.newInstance();
        query.setRole_id(role_id);
        query.setResource_id(resource_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        return one(query, returnFields);
    }

    @Override
    public List<RoleResourcePrivilegeResultDomain> list(RoleResourcePrivilegeQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<RoleResourcePrivilegeResultDomain> pagination(RoleResourcePrivilegeQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

}
