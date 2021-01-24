package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.component.foura.dao.IGroupRoleDao;
import liangchen.wang.gradf.component.foura.dao.entity.GroupRole;
import liangchen.wang.gradf.component.foura.dao.query.GroupRoleQuery;
import liangchen.wang.gradf.component.foura.manager.IGroupRoleManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.GroupRoleParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.GroupRoleResultDomain;
import liangchen.wang.gradf.component.foura.utils.FouraUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LiangChen.Wang 2020-10-08 19:11:17
 */
@Component("Gradf_Foura_DefaultGroupRoleManager")
public class GroupRoleManagerImpl extends AbstractManager<GroupRole, GroupRoleQuery, GroupRoleResultDomain> implements IGroupRoleManager {
    @Inject
    public GroupRoleManagerImpl(@Named("Gradf_Foura_DefaultGroupRoleDao") IGroupRoleDao dao) {
        super("群组角色", "GroupRole", dao);
    }

    @Override
    public boolean insert(GroupRoleParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        parameter.populateEntity((groupRole) -> {
            //TODO 这里可以调整Entity，比如设置主键/状态等
            Assert.INSTANCE.notBlankElseRun(groupRole.getStatus(), () -> groupRole.setStatus(Status.NORMAL.name()));
            groupRole.initFields();
        });
        return super.insert(parameter);
    }

    @Override
    public boolean deleteByPrimaryKey(Long group_id, Long role_id) {
        return updateStatusByPrimaryKey(group_id, role_id, Status.DELETED.name());
    }

    @Override
    public boolean updateByPrimaryKey(GroupRoleParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        GroupRoleQuery query = GroupRoleQuery.newInstance();
        query.setGroup_id(parameter.getGroup_id());
        query.setRole_id(parameter.getRole_id());
        int rows = updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(GroupRoleParameterDomain parameter, GroupRoleQuery query) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        parameter.populateEntity((groupRole) -> {
            // TODO 这里添加不更新或者不论是否空值总更新的字段
            groupRole.setModify_datetime(LocalDateTime.now());
            groupRole.setModifier(FouraUtil.INSTANCE.getOperator());
        });
        return super.updateByQuery(parameter, query);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long group_id, Long role_id, String statusTo, String... statusFrom) {
        return updateStatusByPrimaryKey(group_id, role_id, statusTo, statusFrom, null);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long group_id, Long role_id, String statusTo, String[] statusIn, String[] statusNotIn) {
        Assert.INSTANCE.notNull(group_id, "参数不能为空");
        Assert.INSTANCE.notNull(role_id, "参数不能为空");
        Assert.INSTANCE.notBlank(statusTo, "状态不能为空");
        GroupRoleParameterDomain parameter = GroupRoleParameterDomain.newInstance();
        parameter.setStatus(statusTo);
        GroupRoleQuery query = GroupRoleQuery.newInstance();
        query.setGroup_id(group_id);
        query.setRole_id(role_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        int rows = super.updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public GroupRoleResultDomain byPrimaryKey(Long group_id, Long role_id, String... returnFields) {
        return byPrimaryKey(group_id, role_id, null, null, returnFields);
    }

    @Override
    public GroupRoleResultDomain byPrimaryKeyOrThrow(Long group_id, Long role_id, String... returnFields) {
        return byPrimaryKeyOrThrow(group_id, role_id, null, null, returnFields);
    }

    @Override
    public GroupRoleResultDomain byPrimaryKeyOrThrow(Long group_id, Long role_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        GroupRoleResultDomain resultDomain = byPrimaryKey(group_id, role_id, statusIn, statusNotIn, returnFields);
        Assert.INSTANCE.notNull(resultDomain, "数据不存在或者状态错误");
        return resultDomain;
    }

    @Override
    public GroupRoleResultDomain byPrimaryKey(Long group_id, Long role_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        Assert.INSTANCE.notNull(group_id, "参数不能为空");
        Assert.INSTANCE.notNull(role_id, "参数不能为空");
        GroupRoleQuery query = GroupRoleQuery.newInstance();
        query.setGroup_id(group_id);
        query.setRole_id(role_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        return one(query, returnFields);
    }

    @Override
    public List<GroupRoleResultDomain> list(GroupRoleQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<GroupRoleResultDomain> pagination(GroupRoleQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

}
