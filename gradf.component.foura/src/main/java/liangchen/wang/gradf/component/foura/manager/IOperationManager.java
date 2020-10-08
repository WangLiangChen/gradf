package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.dao.query.OperationQuery;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.OperationParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.OperationResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2020-04-12 23:47:02
 */
public interface IOperationManager {

    boolean insert(OperationParameterDomain parameter);

    boolean deleteByPrimaryKey(Long operation_id);

    boolean updateByPrimaryKey(OperationParameterDomain parameter);

    int updateByQuery(OperationParameterDomain parameter, OperationQuery query);


    OperationResultDomain byPrimaryKey(Long operation_id, String... returnFields);

    OperationResultDomain byPrimaryKeyOrThrow(Long operation_id, String... returnFields);


    List<OperationResultDomain> list(OperationQuery query, String... returnFields);

    PaginationResult<OperationResultDomain> pagination(OperationQuery query, String... returnFields);

}
