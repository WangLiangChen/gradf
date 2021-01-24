package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.component.foura.dao.IRoleDao;
import liangchen.wang.gradf.component.foura.dao.entity.Role;
import liangchen.wang.gradf.component.foura.dao.query.RoleQuery;
import liangchen.wang.gradf.component.foura.manager.IRoleManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.RoleParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleResultDomain;
import liangchen.wang.gradf.component.foura.utils.FouraUtil;
import liangchen.wang.gradf.framework.cluster.utils.LockUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.commons.validator.AssertLevel;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.data.utils.UidDb;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LiangChen.Wang 2019-12-23 23:23:40
 */
@Component("Gradf_Foura_DefaultRoleManager")
public class RoleManagerImpl extends AbstractManager<Role, RoleQuery, RoleResultDomain> implements IRoleManager {

    @Inject
    public RoleManagerImpl(@Named("Gradf_Foura_DefaultRoleDao") IRoleDao dao) {
        super("角色", "Role", dao);
    }

    @Override
    public boolean insert(RoleParameterDomain parameter) {
        Assert.INSTANCE.validate(parameter, AssertLevel.INFO);
        parameter.populateEntity((role) -> {
            Assert.INSTANCE.notNullElseRun(role.getRole_id(), () -> role.setRole_id(UidDb.INSTANCE.uid()));
            Assert.INSTANCE.notBlankElseRun(role.getStatus(), () -> role.setStatus(Status.NORMAL.name()));
            role.initFields();
        });
        String role_key = parameter.getRole_key();
        if (StringUtil.INSTANCE.isBlank(role_key)) {
            return super.insert(parameter);
        }
        // 加锁判重
        return LockUtil.INSTANCE.executeInLock("ROLE_INSERT", () -> {
            RoleQuery query = RoleQuery.newInstance();
            query.setRole_key(role_key);
            boolean exist = exist(query);
            Assert.INSTANCE.isFalse(exist, AssertLevel.PROMPT, "角色重复");
            return super.insert(parameter);
        });
    }

    @Override
    public boolean deleteByPrimaryKey(Long role_id) {
        return updateStatusByPrimaryKey(role_id, Status.DELETED.name());
    }

    @Override
    public boolean updateByPrimaryKey(RoleParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        RoleQuery query = RoleQuery.newInstance();
        query.setRole_id(parameter.getRole_id());
        int rows = updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(RoleParameterDomain parameter, RoleQuery query) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        parameter.populateEntity((role) -> {
            role.setModify_datetime(LocalDateTime.now());
            role.setModifier(FouraUtil.INSTANCE.getOperator());
            // role_key不更新
            role.setRole_key(null);
            // summary强制更新
            String summary = role.getSummary();
            role.setSummary(null);
            role.put("summary", summary);
        });
        return super.updateByQuery(parameter, query);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long role_id, String statusTo, String... statusFrom) {
        return updateStatusByPrimaryKey(role_id, statusTo, statusFrom, null);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long role_id, String statusTo, String[] statusIn, String[] statusNotIn) {
        Assert.INSTANCE.notNull(role_id, "角色ID不能为空");
        Assert.INSTANCE.notBlank(statusTo, "状态不能为空");
        RoleParameterDomain parameter = RoleParameterDomain.newInstance();
        parameter.setStatus(statusTo);
        RoleQuery query = RoleQuery.newInstance();
        query.setRole_id(role_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        int rows = super.updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public RoleResultDomain byPrimaryKey(Long role_id, String... returnFields) {
        return byPrimaryKey(role_id, null, null, returnFields);
    }

    @Override
    public RoleResultDomain byPrimaryKeyOrThrow(Long role_id, String... returnFields) {
        return byPrimaryKeyOrThrow(role_id, null, null, returnFields);
    }

    @Override
    public RoleResultDomain byPrimaryKeyOrThrow(Long role_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        RoleResultDomain resultDomain = byPrimaryKey(role_id, statusIn, statusNotIn, returnFields);
        Assert.INSTANCE.notNull(resultDomain, "数据不存在或者状态错误");
        return resultDomain;
    }

    @Override
    public RoleResultDomain byPrimaryKey(Long role_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        Assert.INSTANCE.notNull(role_id, "角色ID不能为空");
        RoleQuery query = RoleQuery.newInstance();
        query.setRole_id(role_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        return one(query, returnFields);
    }

    @Override
    public List<RoleResultDomain> list(RoleQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<RoleResultDomain> pagination(RoleQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

    @Override
    public boolean exist(Long role_id) {
        Assert.INSTANCE.notNull(role_id, "角色ID不能为空");
        RoleQuery query = RoleQuery.newInstance();
        query.setRole_id(role_id);
        return super.exist(query);
    }

    @Override
    public Long idByKey(String role_key) {
        Assert.INSTANCE.notBlank(role_key, "角色Key不能为空");
        RoleQuery query = RoleQuery.newInstance();
        query.setRole_key(role_key);
        RoleResultDomain roleResultDomain = one(query, "role_id");
        Assert.INSTANCE.notNull(roleResultDomain, "数据不存在");
        return roleResultDomain.getRole_id();
    }

    @Override
    public String keyById(Long role_id) {
        Assert.INSTANCE.notNull(role_id, "角色ID不能为空");
        RoleResultDomain resultDomain = byPrimaryKeyOrThrow(role_id, "role_key");
        Assert.INSTANCE.notNull(resultDomain, "数据不存在");
        return resultDomain.getRole_key();
    }
}
