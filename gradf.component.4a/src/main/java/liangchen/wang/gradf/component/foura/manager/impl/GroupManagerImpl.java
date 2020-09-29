package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.web.base.AbstractManager;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.commons.utils.ContextUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.commons.validator.AssertLevel;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.framework.data.utils.UidDb;
import liangchen.wang.gradf.distributed.utils.LockUtil;
import liangchen.wang.gradf.component.foura.dao.IGroupDao;
import liangchen.wang.gradf.component.foura.dao.entity.Group;
import liangchen.wang.gradf.component.foura.dao.query.GroupQuery;
import liangchen.wang.gradf.component.foura.manager.IGroupManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.GroupParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.GroupResultDomain;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LiangChen.Wang 2020-04-12 01:44:04
 */
@Component("Gradf_Foura_DefaultGroupManager")
public class GroupManagerImpl extends AbstractManager<Group, GroupResultDomain> implements IGroupManager {
    @Inject
    public GroupManagerImpl(@Named("Gradf_Foura_DefaultGroupDao") IGroupDao dao) {
        super("群组", "Group", dao);
    }

    @Override
    public boolean insert(GroupParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, AssertLevel.INFO,"参数不能为空");
        parameter.populateEntity((group) -> {
            Assert.INSTANCE.notNullElseRun(group.getGroup_id(), () -> group.setGroup_id(UidDb.INSTANCE.uid()));
            Assert.INSTANCE.notBlank(group.getStatus(), () -> group.setStatus(Status.NORMAL.name()));
            group.initOperator();
            group.initFields();
        });
        String group_key = parameter.getGroup_key();
        if (StringUtil.INSTANCE.isBlank(group_key)) {
            return super.insert(parameter);
        }
        // 加锁判重
        return LockUtil.INSTANCE.executeInLock("GROUP_INSERT", () -> {
            GroupQuery query = GroupQuery.newInstance();
            query.setGroup_key(group_key);
            boolean exist = exist(query);
            Assert.INSTANCE.isFalse(exist, AssertLevel.PROMPT, "群组重复");
            return super.insert(parameter);
        });
    }

    @Override
    public boolean deleteByPrimaryKey(Long group_id) {
        return updateStatusByPrimaryKey(group_id, Status.DELETED.name());
    }

    @Override
    public boolean updateByPrimaryKey(GroupParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        GroupQuery query = GroupQuery.newInstance();
        query.setGroup_id(parameter.getGroup_id());
        int rows = updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(GroupParameterDomain parameter, GroupQuery query) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        parameter.populateEntity((group) -> {
            group.setModify_datetime(LocalDateTime.now());
            group.setModifier(ContextUtil.INSTANCE.getOperator());
            // group_key不更新
            group.setGroup_key(null);
            // summary强制更新
            String summary = group.getSummary();
            group.setSummary(null);
            group.put("summary", summary);
        });
        return super.updateByQuery(parameter, query);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long group_id, String statusTo, String... statusFrom) {
        return updateStatusByPrimaryKey(group_id, statusTo, statusFrom, null);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long group_id, String statusTo, String[] statusIn, String[] statusNotIn) {
        Assert.INSTANCE.notBlank(group_id, "群组ID不能为空");
        Assert.INSTANCE.notBlank(statusTo, "状态不能为空");
        GroupParameterDomain parameter = GroupParameterDomain.newInstance();
        parameter.setStatus(statusTo);
        GroupQuery query = GroupQuery.newInstance();
        query.setGroup_id(group_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        int rows = super.updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public GroupResultDomain byPrimaryKey(Long group_id, String... returnFields) {
        return byPrimaryKey(group_id, null, null, returnFields);
    }

    @Override
    public GroupResultDomain byPrimaryKeyOrThrow(Long group_id, String... returnFields) {
        return byPrimaryKeyOrThrow(group_id, null, null, returnFields);
    }

    @Override
    public GroupResultDomain byPrimaryKeyOrThrow(Long group_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        GroupResultDomain resultDomain = byPrimaryKey(group_id, statusIn, statusNotIn, returnFields);
        Assert.INSTANCE.notNull(resultDomain, "数据不存在或者状态错误");
        return resultDomain;
    }

    @Override
    public GroupResultDomain byPrimaryKey(Long group_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        Assert.INSTANCE.notNull(group_id, "群组ID不能为空");
        GroupQuery query = GroupQuery.newInstance();
        query.setGroup_id(group_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        return one(query, returnFields);
    }

    @Override
    public List<GroupResultDomain> list(GroupQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<GroupResultDomain> pagination(GroupQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

    @Override
    public Long idByKey(String group_key) {
        Assert.INSTANCE.notBlank(group_key, "群组Key不能为空");
        GroupQuery query = GroupQuery.newInstance();
        query.setGroup_key(group_key);
        GroupResultDomain groupResultDomain = one(query, "group_id");
        Assert.INSTANCE.notNull(groupResultDomain, "数据不存在");
        return groupResultDomain.getGroup_id();
    }

}
