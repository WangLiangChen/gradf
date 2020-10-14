package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.dao.query.ResourceQuery;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.ResourceParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.ResourceResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2019-12-25 23:17:21
 */
public interface IResourceManager {

    boolean insert(ResourceParameterDomain parameter);

    boolean deleteByPrimaryKey(Long resource_id);

    boolean updateByPrimaryKey(ResourceParameterDomain parameter);

    int updateByQuery(ResourceParameterDomain parameter, ResourceQuery query);

    ResourceResultDomain byPrimaryKey(Long resource_id, String... returnFields);

    ResourceResultDomain byPrimaryKeyOrThrow(Long resource_id, String... returnFields);

    List<ResourceResultDomain> list(ResourceQuery query, String... returnFields);

    PaginationResult<ResourceResultDomain> pagination(ResourceQuery query, String... returnFields);

    Long idByKey(String resource_key);

    String keyById(Long resource_id);
}
