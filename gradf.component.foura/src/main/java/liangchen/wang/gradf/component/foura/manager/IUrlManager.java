package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.dao.query.UrlQuery;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.UrlParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.UrlResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2020-04-13 00:40:41
 */
public interface IUrlManager {

    boolean insert(UrlParameterDomain parameter);

    boolean deleteByPrimaryKey(Long url_id);

    boolean updateByPrimaryKey(UrlParameterDomain parameter);

    int updateByQuery(UrlParameterDomain parameter, UrlQuery query);

    UrlResultDomain byPrimaryKey(Long url_id, String... returnFields);

    UrlResultDomain byPrimaryKeyOrThrow(Long url_id, String... returnFields);


    List<UrlResultDomain> list(UrlQuery query, String... returnFields);

    PaginationResult<UrlResultDomain> pagination(UrlQuery query, String... returnFields);

    String urlPathById(Long url_id);
}
