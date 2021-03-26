package liangchen.wang.gradf.component.business.dao.impl;

import liangchen.wang.gradf.component.business.dao.IRegionDao;
import liangchen.wang.gradf.component.business.dao.entity.Region;
import liangchen.wang.gradf.component.business.dao.query.RegionQuery;

import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2019-12-29 20:30:26
 */
@Repository("Gradf_Business_DefaultRegionDao")
public class RegionDaoImpl extends AbstractJdbcDao<Region, RegionQuery> implements IRegionDao {

}
