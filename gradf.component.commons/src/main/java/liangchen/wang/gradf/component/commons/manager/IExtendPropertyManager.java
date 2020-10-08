package liangchen.wang.gradf.component.commons.manager;


import liangchen.wang.gradf.component.commons.dao.query.ExtendPropertyQuery;
import liangchen.wang.gradf.component.commons.manager.domain.parameter.ExtendPropertyParameterDomain;
import liangchen.wang.gradf.component.commons.manager.domain.result.ExtendPropertyResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2019-12-29 20:32:33
 */
public interface IExtendPropertyManager {

    boolean insert(ExtendPropertyParameterDomain parameter);

    boolean deleteByPrimaryKey(Long property_id);

    int deleteByQuery(ExtendPropertyQuery query);

    boolean updateByPrimaryKey(ExtendPropertyParameterDomain parameter);

    int updateByQuery(ExtendPropertyParameterDomain parameter, ExtendPropertyQuery query);

    ExtendPropertyResultDomain byPrimaryKey(Long property_id);

    ExtendPropertyResultDomain byPrimaryKeyOrThrow(Long property_id);

    List<ExtendPropertyResultDomain> list(ExtendPropertyQuery query, String... returnFields);

    PaginationResult<ExtendPropertyResultDomain> pagination(ExtendPropertyQuery query, String... returnFields);

}
