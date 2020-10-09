package liangchen.wang.gradf.component.business.manager;

import liangchen.wang.gradf.component.business.dao.query.RegionQuery;
import liangchen.wang.gradf.component.business.manager.domain.parameter.RegionParameterDomain;
import liangchen.wang.gradf.component.business.manager.domain.result.RegionResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2019-12-29 20:30:26
 */
public interface IRegionManager {

    boolean deleteByPrimaryKey(Long region_code);

    boolean updateByPrimaryKey(RegionParameterDomain parameter);

    int updateByQuery(RegionParameterDomain parameter, RegionQuery query);

    boolean updateStatusByPrimaryKey(Long region_code, String statusTo, String... statusFrom);

    boolean updateStatusByPrimaryKey(Long region_code, String statusTo, String[] statusIn, String[] statusNotIn);

    RegionResultDomain byPrimaryKey(Long region_code, String... returnFields);

    RegionResultDomain byPrimaryKeyOrThrow(Long region_code, String... returnFields);

    RegionResultDomain byPrimaryKeyOrThrow(Long region_code, String[] statusIn, String[] statusNotIn, String... returnFields);

    RegionResultDomain byPrimaryKey(Long region_code, String[] statusIn, String[] statusNotIn, String... returnFields);

    List<RegionResultDomain> list(RegionQuery query, String... returnFields);

    PaginationResult<RegionResultDomain> pagination(RegionQuery query, String... returnFields);

}
