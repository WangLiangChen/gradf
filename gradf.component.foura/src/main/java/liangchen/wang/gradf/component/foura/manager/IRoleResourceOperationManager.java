package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.dao.query.RoleResourceOperationQuery;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.RoleResourceOperationParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleResourceOperationResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2020-04-12 23:55:49
 */
public interface IRoleResourceOperationManager {

    boolean insert(RoleResourceOperationParameterDomain parameter);

    boolean deleteByPrimaryKey(Long role_id, Long resource_id, Long operation_id);

    boolean updateByPrimaryKey(RoleResourceOperationParameterDomain parameter);

    int updateByQuery(RoleResourceOperationParameterDomain parameter, RoleResourceOperationQuery query);

    boolean updateStatusByPrimaryKey(Long role_id, Long resource_id, Long operation_id, String statusTo, String... statusFrom);

    boolean updateStatusByPrimaryKey(Long role_id, Long resource_id, Long operation_id, String statusTo, String[] statusIn, String[] statusNotIn);

    RoleResourceOperationResultDomain byPrimaryKey(Long role_id, Long resource_id, Long operation_id, String... returnFields);

    RoleResourceOperationResultDomain byPrimaryKeyOrThrow(Long role_id, Long resource_id, Long operation_id, String... returnFields);

    RoleResourceOperationResultDomain byPrimaryKeyOrThrow(Long role_id, Long resource_id, Long operation_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    RoleResourceOperationResultDomain byPrimaryKey(Long role_id, Long resource_id, Long operation_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    List<RoleResourceOperationResultDomain> list(RoleResourceOperationQuery query, String... returnFields);

    PaginationResult<RoleResourceOperationResultDomain> pagination(RoleResourceOperationQuery query, String... returnFields);

}
