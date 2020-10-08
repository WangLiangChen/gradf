package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.dao.query.GroupRoleQuery;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.GroupRoleParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.GroupRoleResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2020-10-08 19:11:17
*/
public interface IGroupRoleManager  {

    boolean insert(GroupRoleParameterDomain parameter);

    boolean deleteByPrimaryKey(Long group_id, Long role_id);

    boolean updateByPrimaryKey(GroupRoleParameterDomain parameter);

    int updateByQuery(GroupRoleParameterDomain parameter, GroupRoleQuery query);

    boolean updateStatusByPrimaryKey(Long group_id, Long role_id, String statusTo, String... statusFrom);

    boolean updateStatusByPrimaryKey(Long group_id, Long role_id, String statusTo, String[] statusIn, String[] statusNotIn);

    GroupRoleResultDomain byPrimaryKey(Long group_id, Long role_id, String... returnFields);

    GroupRoleResultDomain byPrimaryKeyOrThrow(Long group_id, Long role_id, String... returnFields);

    GroupRoleResultDomain byPrimaryKeyOrThrow(Long group_id, Long role_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    GroupRoleResultDomain byPrimaryKey(Long group_id, Long role_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    List<GroupRoleResultDomain> list(GroupRoleQuery query, String... returnFields);

    PaginationResult<GroupRoleResultDomain> pagination(GroupRoleQuery query, String... returnFields);

}
