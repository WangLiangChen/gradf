package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.dao.query.RoleAccountQuery;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.RoleAccountParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleAccountResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;
import java.util.Set;

/**
 * @author LiangChen.Wang 2019-12-23 23:24:09
 */
public interface IRoleAccountManager {

    boolean insert(RoleAccountParameterDomain parameter);

    boolean deleteByPrimaryKey(Long role_id, Long account_id);


    RoleAccountResultDomain byPrimaryKey(Long role_id, Long account_id, String... returnFields);

    RoleAccountResultDomain byPrimaryKeyOrThrow(Long role_id, Long account_id, String... returnFields);

    List<RoleAccountResultDomain> list(RoleAccountQuery query, String... returnFields);

    PaginationResult<RoleAccountResultDomain> pagination(RoleAccountQuery query, String... returnFields);

    boolean exist(Long role_id, Long account_id);

    Set<Long> roleIdsByAccountId(Long account_id);

    Set<Long> accountIdsByRoleId(Long role_id);
}
