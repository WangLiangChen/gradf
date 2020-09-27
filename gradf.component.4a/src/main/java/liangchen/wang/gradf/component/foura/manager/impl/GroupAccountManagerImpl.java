package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.business.base.AbstractManager;
import liangchen.wang.gradf.framework.commons.exeception.PromptException;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.commons.validator.AssertLevel;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.component.foura.dao.IGroupAccountDao;
import liangchen.wang.gradf.component.foura.dao.entity.GroupAccount;
import liangchen.wang.gradf.component.foura.dao.query.GroupAccountQuery;
import liangchen.wang.gradf.component.foura.manager.IGroupAccountManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.GroupAccountParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.GroupAccountResultDomain;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LiangChen.Wang 2019-12-25 23:16:39
 */
@Component("Crdf_Foura_DefaultGroupAccountManager")
public class GroupAccountManagerImpl extends AbstractManager<GroupAccount, GroupAccountResultDomain> implements IGroupAccountManager {
    @Inject
    public GroupAccountManagerImpl(@Named("Crdf_Foura_DefaultGroupAccountDao") IGroupAccountDao dao) {
        super("群组账户关系", "GroupAccount", dao);
    }

    @Override
    public boolean insert(GroupAccountParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, AssertLevel.INFO, "参数不能为空");
        parameter.populateEntity((groupAccount) -> {
            Assert.INSTANCE.notBlank(groupAccount.getStatus(), () -> groupAccount.setStatus(Status.NORMAL.name()));
            groupAccount.initOperator();
            groupAccount.initFields();
        });
        try {
            return super.insert(parameter);
        } catch (DuplicateKeyException e) {
            throw new PromptException("群组账户重复");
        }
    }

    @Override
    public boolean deleteByPrimaryKey(Long group_id, Long account_id) {
        Assert.INSTANCE.notNull(group_id, AssertLevel.INFO, "group_id不能为空");
        Assert.INSTANCE.notNull(account_id, AssertLevel.INFO, "account_id不能为空");
        GroupAccountQuery query = GroupAccountQuery.newInstance();
        query.setGroup_id(group_id);
        query.setAccount_id(account_id);
        int rows = deleteByQuery(query);
        return rows == 1;
    }


    @Override
    public GroupAccountResultDomain byPrimaryKey(Long group_id, Long account_id, String... returnFields) {
        Assert.INSTANCE.notNull(group_id, AssertLevel.INFO, "group_id不能为空");
        Assert.INSTANCE.notNull(account_id, AssertLevel.INFO, "account_id不能为空");
        GroupAccountQuery query = GroupAccountQuery.newInstance();
        query.setGroup_id(group_id);
        query.setAccount_id(account_id);
        return one(query, returnFields);
    }

    @Override
    public GroupAccountResultDomain byPrimaryKeyOrThrow(Long group_id, Long account_id, String... returnFields) {
        GroupAccountResultDomain groupAccountResultDomain = byPrimaryKey(group_id, account_id, returnFields);
        Assert.INSTANCE.notNull(groupAccountResultDomain, AssertLevel.INFO, "数据不存在");
        return groupAccountResultDomain;
    }

    @Override
    public List<GroupAccountResultDomain> list(GroupAccountQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<GroupAccountResultDomain> pagination(GroupAccountQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

    @Override
    public boolean exist(Long group_id, Long account_id) {
        Assert.INSTANCE.notNull(group_id, AssertLevel.INFO, "group_id不能为空");
        Assert.INSTANCE.notNull(account_id, AssertLevel.INFO, "account_id不能为空");
        GroupAccountQuery query = GroupAccountQuery.newInstance();
        query.setGroup_id(group_id);
        query.setAccount_id(account_id);
        int count = super.count(query);
        return count == 1;
    }

    @Override
    public Set<Long> groupIdsByAccountId(Long account_id) {
        Assert.INSTANCE.notNull(account_id, AssertLevel.INFO, "账户ID不能为空");
        GroupAccountQuery query = GroupAccountQuery.newInstance();
        query.setAccount_id(account_id);
        List<GroupAccountResultDomain> list = super.list(query, "group_id");
        return list.stream().map(e -> e.getGroup_id()).collect(Collectors.toSet());
    }

    @Override
    public Set<Long> accountIdsByGroupId(Long group_id) {
        Assert.INSTANCE.notNull(group_id, AssertLevel.INFO, "群组ID不能为空");
        GroupAccountQuery query = GroupAccountQuery.newInstance();
        query.setGroup_id(group_id);
        List<GroupAccountResultDomain> list = super.list(query, "account_id");
        return list.stream().map(e -> e.getGroup_id()).collect(Collectors.toSet());
    }

}
