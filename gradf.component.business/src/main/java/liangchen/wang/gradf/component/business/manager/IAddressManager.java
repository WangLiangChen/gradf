package liangchen.wang.gradf.component.business.manager;

import liangchen.wang.gradf.component.business.dao.query.AddressQuery;
import liangchen.wang.gradf.component.business.manager.domain.parameter.AddressParameterDomain;
import liangchen.wang.gradf.component.business.manager.domain.result.AddressResultDomain;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;

import java.util.List;

/**
 * @author LiangChen.Wang 2020-03-24 08:35:30
*/
public interface IAddressManager  {

    boolean insert(AddressParameterDomain parameter);

    boolean deleteByPrimaryKey(Long address_id);

    boolean updateByPrimaryKey(AddressParameterDomain parameter);

    int updateByQuery(AddressParameterDomain parameter, AddressQuery query);

    boolean updateStatusByPrimaryKey(Long address_id, String statusTo, String... statusFrom);

    boolean updateStatusByPrimaryKey(Long address_id, String statusTo, String[] statusIn, String[] statusNotIn);

    AddressResultDomain byPrimaryKey(Long address_id, String... returnFields);

    AddressResultDomain byPrimaryKeyOrThrow(Long address_id, String... returnFields);

    AddressResultDomain byPrimaryKeyOrThrow(Long address_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    AddressResultDomain byPrimaryKey(Long address_id, String[] statusIn, String[] statusNotIn, String... returnFields);

    List<AddressResultDomain> list(AddressQuery query, String... returnFields);

    PaginationResult<AddressResultDomain> pagination(AddressQuery query, String... returnFields);

}
