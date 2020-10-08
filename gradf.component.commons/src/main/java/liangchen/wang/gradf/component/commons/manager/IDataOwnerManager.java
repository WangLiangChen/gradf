package liangchen.wang.gradf.component.commons.manager;


import liangchen.wang.gradf.component.commons.dao.query.DataOwnerQuery;
import liangchen.wang.gradf.component.commons.manager.domain.parameter.DataOwnerParameterDomain;
import liangchen.wang.gradf.component.commons.manager.domain.result.DataOwnerResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2019-12-29 20:29:37
 */
public interface IDataOwnerManager {

    boolean insert(DataOwnerParameterDomain parameter);

    boolean deleteByPrimaryKey(Long record_id);

    int deleteByQuery(DataOwnerQuery query);

    boolean updateByPrimaryKey(DataOwnerParameterDomain parameter);

    int updateByQuery(DataOwnerParameterDomain parameter, DataOwnerQuery query);

    DataOwnerResultDomain byPrimaryKey(Long record_id);

    DataOwnerResultDomain byPrimaryKeyOrThrow(Long record_id);

    List<DataOwnerResultDomain> list(DataOwnerQuery query, String... returnFields);

    PaginationResult<DataOwnerResultDomain> pagination(DataOwnerQuery query, String... returnFields);

}
