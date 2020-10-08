package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.dao.query.RoleResourcePrivilegeQuery;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.RoleResourcePrivilegeParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleResourcePrivilegeResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2020-04-12 23:55:23
 */
public interface IRoleResourcePrivilegeManager {

    boolean insert(RoleResourcePrivilegeParameterDomain parameter);

    boolean deleteByPrimaryKey(Long role_id, Long resource_id);

    boolean updateByPrimaryKey(RoleResourcePrivilegeParameterDomain parameter);

    int updateByQuery(RoleResourcePrivilegeParameterDomain parameter, RoleResourcePrivilegeQuery query);

    boolean updateStatusByPrimaryKey(Long role_id, Long resource_id, String statusTo, String... statusFrom);

    boolean updateStatusByPrimaryKey(Long role_id, Long resource_id, String statusTo, String[] statusIn, String[] statusNotIn);

    RoleResourcePrivilegeResultDomain byPrimaryKey(Long role_id, Long resource_id, String... returnFields);

    RoleResourcePrivilegeResultDomain byPrimaryKeyOrThrow(Long role_id, Long resource_id, String... returnFields);

    RoleResourcePrivilegeResultDomain byPrimaryKeyOrThrow(Long role_id, Long resource_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    RoleResourcePrivilegeResultDomain byPrimaryKey(Long role_id, Long resource_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    List<RoleResourcePrivilegeResultDomain> list(RoleResourcePrivilegeQuery query, String... returnFields);

    PaginationResult<RoleResourcePrivilegeResultDomain> pagination(RoleResourcePrivilegeQuery query, String... returnFields);

}
