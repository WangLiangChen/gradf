package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.component.foura.dao.query.GroupAccountQuery;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.GroupAccountParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.GroupAccountResultDomain;

import java.util.List;
import java.util.Set;

/**
 * @author LiangChen.Wang 2019-12-25 23:16:39
*/
public interface IGroupAccountManager  {

    boolean insert(GroupAccountParameterDomain parameter);

    boolean deleteByPrimaryKey(Long group_id, Long account_id);


    GroupAccountResultDomain byPrimaryKey(Long group_id, Long account_id, String... returnFields);

    GroupAccountResultDomain byPrimaryKeyOrThrow(Long group_id, Long account_id, String... returnFields);

    List<GroupAccountResultDomain> list(GroupAccountQuery query, String... returnFields);

    PaginationResult<GroupAccountResultDomain> pagination(GroupAccountQuery query, String... returnFields);

    boolean exist(Long group_id, Long account_id);

    Set<Long> groupIdsByAccountId(Long account_id);

    Set<Long> accountIdsByGroupId(Long group_id);
}
