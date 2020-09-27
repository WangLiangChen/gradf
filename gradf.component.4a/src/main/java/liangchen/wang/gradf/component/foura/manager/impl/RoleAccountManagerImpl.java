package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.business.base.AbstractManager;
import liangchen.wang.gradf.framework.commons.exeception.PromptException;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.commons.validator.AssertLevel;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.component.foura.dao.IRoleAccountDao;
import liangchen.wang.gradf.component.foura.dao.entity.RoleAccount;
import liangchen.wang.gradf.component.foura.dao.query.RoleAccountQuery;
import liangchen.wang.gradf.component.foura.manager.IRoleAccountManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.RoleAccountParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleAccountResultDomain;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LiangChen.Wang 2019-12-23 23:24:09
 */
@Component("Crdf_Foura_DefaultRoleAccountManager")
public class RoleAccountManagerImpl extends AbstractManager<RoleAccount, RoleAccountResultDomain> implements IRoleAccountManager {
    @Inject
    public RoleAccountManagerImpl(@Named("Crdf_Foura_DefaultRoleAccountDao") IRoleAccountDao dao) {
        super("角色账户关系", "RoleAccount", dao);
    }

    @Override
    public boolean insert(RoleAccountParameterDomain parameter) {
        parameter.populateEntity((entity) -> {
            RoleAccount roleAccount = (RoleAccount) entity;
            Assert.INSTANCE.notBlank(roleAccount.getStatus(), () -> roleAccount.setStatus(Status.NORMAL.name()));
            roleAccount.initOperator();
            ;
            roleAccount.initFields();
        });
        try {
            return super.insert(parameter);
        } catch (DuplicateKeyException e) {
            throw new PromptException("数据重复");
        }

    }

    @Override
    public boolean deleteByPrimaryKey(Long role_id, Long account_id) {
        Assert.INSTANCE.notNull(role_id, AssertLevel.INFO, "role_id不能为空");
        Assert.INSTANCE.notNull(account_id, AssertLevel.INFO, "account_id不能为空");
        RoleAccountQuery query = RoleAccountQuery.newInstance();
        query.setRole_id(role_id);
        query.setAccount_id(account_id);
        return deleteByQuery(query) == 1;
    }


    @Override
    public RoleAccountResultDomain byPrimaryKey(Long role_id, Long account_id, String... returnFields) {
        Assert.INSTANCE.notNull(role_id, AssertLevel.INFO, "role_id不能为空");
        Assert.INSTANCE.notNull(account_id, AssertLevel.INFO, "account_id不能为空");
        RoleAccountQuery query = RoleAccountQuery.newInstance();
        query.setRole_id(role_id);
        query.setAccount_id(account_id);
        return one(query, returnFields);
    }

    @Override
    public RoleAccountResultDomain byPrimaryKeyOrThrow(Long role_id, Long account_id, String... returnFields) {
        RoleAccountResultDomain roleAccountResultDomain = byPrimaryKey(role_id, account_id, returnFields);
        Assert.INSTANCE.notNull(roleAccountResultDomain, AssertLevel.INFO, "数据不存在");
        return roleAccountResultDomain;
    }

    @Override
    public List<RoleAccountResultDomain> list(RoleAccountQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<RoleAccountResultDomain> pagination(RoleAccountQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

    @Override
    public boolean exist(Long role_id, Long account_id) {
        Assert.INSTANCE.notNull(role_id, AssertLevel.INFO, "role_id不能为空");
        Assert.INSTANCE.notNull(account_id, AssertLevel.INFO, "account_id不能为空");
        RoleAccountQuery query = RoleAccountQuery.newInstance();
        query.setRole_id(role_id);
        query.setAccount_id(account_id);
        int count = super.count(query);
        return count == 1;
    }

    @Override
    public Set<Long> roleIdsByAccountId(Long account_id) {
        Assert.INSTANCE.notNull(account_id, AssertLevel.INFO, "账户ID不能为空");
        RoleAccountQuery query = RoleAccountQuery.newInstance();
        query.setAccount_id(account_id);
        List<RoleAccountResultDomain> list = super.list(query, "role_id");
        return list.stream().map(e -> e.getRole_id()).collect(Collectors.toSet());
    }

    @Override
    public Set<Long> accountIdsByRoleId(Long role_id) {
        Assert.INSTANCE.notNull(role_id, AssertLevel.INFO, "角色ID不能为空");
        RoleAccountQuery query = RoleAccountQuery.newInstance();
        query.setRole_id(role_id);
        List<RoleAccountResultDomain> list = super.list(query, "account_id");
        return list.stream().map(e -> e.getRole_id()).collect(Collectors.toSet());
    }
}
