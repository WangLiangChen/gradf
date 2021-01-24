package liangchen.wang.gradf.component.business.dao.impl;

import liangchen.wang.gradf.component.business.dao.IAddressDao;
import liangchen.wang.gradf.component.business.dao.entity.Address;
import liangchen.wang.gradf.component.business.dao.query.AddressQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-03-24 08:35:30
 */
@Repository("Gradf_Business_DefaultAddressDao")
@GradfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class AddressDaoImpl extends AbstractJdbcDao<Address, AddressQuery> implements IAddressDao {

}
