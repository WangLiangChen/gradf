package liangchen.wang.gradf.component.foura.manager;


import liangchen.wang.gradf.component.foura.dao.query.OperationLogQuery;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.OperationLogParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.OperationLogResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2020-03-07 10:24:36
 */
public interface IOperationLogManager {

    void insert(OperationLogParameterDomain parameter);

    OperationLogResultDomain byPrimaryKey(Long log_id, String... returnFields);

    OperationLogResultDomain byPrimaryKeyOrThrow(Long log_id, String... returnFields);

    OperationLogResultDomain byPrimaryKeyOrThrow(Long log_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    OperationLogResultDomain byPrimaryKey(Long log_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    List<OperationLogResultDomain> list(OperationLogQuery query, String... returnFields);

    PaginationResult<OperationLogResultDomain> pagination(OperationLogQuery query, String... returnFields);
}
