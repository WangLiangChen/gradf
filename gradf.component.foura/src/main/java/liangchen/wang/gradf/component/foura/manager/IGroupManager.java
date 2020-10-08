package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.dao.query.GroupQuery;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.GroupParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.GroupResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2020-04-12 01:44:04
 */
public interface IGroupManager {

    boolean insert(GroupParameterDomain parameter);

    boolean deleteByPrimaryKey(Long group_id);

    boolean updateByPrimaryKey(GroupParameterDomain parameter);

    int updateByQuery(GroupParameterDomain parameter, GroupQuery query);

    boolean updateStatusByPrimaryKey(Long group_id, String statusTo, String... statusFrom);

    boolean updateStatusByPrimaryKey(Long group_id, String statusTo, String[] statusIn, String[] statusNotIn);

    GroupResultDomain byPrimaryKey(Long group_id, String... returnFields);

    GroupResultDomain byPrimaryKeyOrThrow(Long group_id, String... returnFields);

    GroupResultDomain byPrimaryKeyOrThrow(Long group_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    GroupResultDomain byPrimaryKey(Long group_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    List<GroupResultDomain> list(GroupQuery query, String... returnFields);

    PaginationResult<GroupResultDomain> pagination(GroupQuery query, String... returnFields);

    Long idByKey(String group_key);
}
