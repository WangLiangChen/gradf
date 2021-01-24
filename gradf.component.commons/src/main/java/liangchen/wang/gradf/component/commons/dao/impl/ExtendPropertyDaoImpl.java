package liangchen.wang.gradf.component.commons.dao.impl;

import liangchen.wang.gradf.component.commons.dao.IExtendPropertyDao;
import liangchen.wang.gradf.component.commons.dao.entity.ExtendProperty;
import liangchen.wang.gradf.component.commons.dao.query.ExtendPropertyQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2019-12-29 20:32:33
 */
@Repository("Gradf_Business_DefaultExtendPropertyDao")
@GradfAutoCacheable(clearMethods = {"insert", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class ExtendPropertyDaoImpl extends AbstractJdbcDao<ExtendProperty, ExtendPropertyQuery> implements IExtendPropertyDao {

}
