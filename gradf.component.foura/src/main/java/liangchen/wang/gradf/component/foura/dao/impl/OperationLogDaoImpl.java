package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IOperationLogDao;
import liangchen.wang.gradf.component.foura.dao.entity.OperationLog;
import liangchen.wang.gradf.component.foura.dao.query.OperationLogQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-03-07 10:24:36
 */
@Repository("Gradf_Business_DefaultOperationLogDao")
@GradfAutoCacheable(clearMethods = {"insert", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class OperationLogDaoImpl extends AbstractBaseDao<OperationLog, OperationLogQuery> implements IOperationLogDao {

}
