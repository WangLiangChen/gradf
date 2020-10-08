package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.dao.query.RoleQuery;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.RoleParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2019-12-23 23:23:40
 */
public interface IRoleManager {

    boolean insert(RoleParameterDomain parameter);

    boolean deleteByPrimaryKey(Long role_id);

    boolean updateByPrimaryKey(RoleParameterDomain parameter);

    int updateByQuery(RoleParameterDomain parameter, RoleQuery query);

    boolean updateStatusByPrimaryKey(Long role_id, String statusTo, String... statusFrom);

    boolean updateStatusByPrimaryKey(Long role_id, String statusTo, String[] statusIn, String[] statusNotIn);

    RoleResultDomain byPrimaryKey(Long role_id, String... returnFields);

    RoleResultDomain byPrimaryKeyOrThrow(Long role_id, String... returnFields);

    RoleResultDomain byPrimaryKeyOrThrow(Long role_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    RoleResultDomain byPrimaryKey(Long role_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    List<RoleResultDomain> list(RoleQuery query, String... returnFields);

    PaginationResult<RoleResultDomain> pagination(RoleQuery query, String... returnFields);

    boolean exist(Long role_id);

    Long idByKey(String roleKey);

    String keyById(Long role_id);
}
