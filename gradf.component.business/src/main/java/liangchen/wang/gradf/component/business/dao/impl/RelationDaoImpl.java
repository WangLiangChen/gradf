package liangchen.wang.gradf.component.business.dao.impl;

import liangchen.wang.gradf.component.business.dao.IRelationDao;
import liangchen.wang.gradf.component.business.dao.entity.Relation;
import liangchen.wang.gradf.component.business.dao.query.RelationQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-19 22:39:49
 */
@Repository("Gradf_Business_DefaultRelationDao")
@GradfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class RelationDaoImpl extends AbstractJdbcDao<Relation, RelationQuery> implements IRelationDao {

}
