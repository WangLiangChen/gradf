package liangchen.wang.gradf.component.business.manager;

import liangchen.wang.gradf.component.business.dao.query.RelationQuery;
import liangchen.wang.gradf.component.business.manager.domain.parameter.RelationParameterDomain;
import liangchen.wang.gradf.component.business.manager.domain.result.RelationResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2020-04-19 22:39:49
 */
public interface IRelationManager {

    boolean insert(RelationParameterDomain parameter);

    boolean deleteByPrimaryKey(Long record_id);

    boolean updateByPrimaryKey(RelationParameterDomain parameter);

    int updateByQuery(RelationParameterDomain parameter, RelationQuery query);

    RelationResultDomain byPrimaryKey(Long record_id);

    RelationResultDomain byPrimaryKeyOrThrow(Long record_id);

    List<RelationResultDomain> list(RelationQuery query, String... returnFields);

    PaginationResult<RelationResultDomain> pagination(RelationQuery query, String... returnFields);

}
