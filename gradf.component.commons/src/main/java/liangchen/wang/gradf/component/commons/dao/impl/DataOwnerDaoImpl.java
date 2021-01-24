package liangchen.wang.gradf.component.commons.dao.impl;

import liangchen.wang.gradf.component.commons.dao.IDataOwnerDao;
import liangchen.wang.gradf.component.commons.dao.entity.DataOwner;
import liangchen.wang.gradf.component.commons.dao.query.DataOwnerQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2019-12-29 20:29:37
 */
@Repository("Gradf_Business_DefaultDataOwnerDao")
@GradfAutoCacheable(clearMethods = {"insert", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class DataOwnerDaoImpl extends AbstractJdbcDao<DataOwner, DataOwnerQuery> implements IDataOwnerDao {

}
