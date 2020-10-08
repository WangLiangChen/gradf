package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.dao.query.UrlRelationQuery;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.UrlRelationParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.UrlRelationResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2020-07-29 13:56:01
 */
public interface IUrlRelationManager {

    boolean insert(UrlRelationParameterDomain parameter);

    List<UrlRelationResultDomain> list(UrlRelationQuery query, String... returnFields);

    PaginationResult<UrlRelationResultDomain> pagination(UrlRelationQuery query, String... returnFields);

}
