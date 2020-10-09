package liangchen.wang.gradf.component.business.dao.impl;

import liangchen.wang.gradf.component.business.dao.IRegionDao;
import liangchen.wang.gradf.component.business.dao.entity.Region;
import liangchen.wang.gradf.component.business.dao.query.RegionQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2019-12-29 20:30:26
 */
@Repository("Gradf_Business_DefaultRegionDao")
@GradfAutoCacheable(clearMethods = {"insert", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class RegionDaoImpl extends AbstractBaseDao<Region, RegionQuery> implements IRegionDao {

}
