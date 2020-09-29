package liangchen.wang.gradf.component.web.dao.impl;

import liangchen.wang.gradf.component.web.dao.IExtendPropertyDao;
import liangchen.wang.gradf.component.web.dao.entity.ExtendProperty;
import liangchen.wang.gradf.component.web.dao.query.ExtendPropertyQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2019-12-29 20:32:33
 */
@Repository("Gradf_Business_DefaultExtendPropertyDao")
@GradfAutoCacheable(clearMethods = {"insert", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class ExtendPropertyDaoImpl extends AbstractBaseDao<ExtendProperty, ExtendPropertyQuery> implements IExtendPropertyDao {

}
