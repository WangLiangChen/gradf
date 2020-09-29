package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.query.RoleResourceOperationQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import liangchen.wang.gradf.component.foura.dao.IRoleResourceOperationDao;
import liangchen.wang.gradf.component.foura.dao.entity.RoleResourceOperation;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-12 23:55:49
*/
@Repository("Gradf_Foura_DefaultRoleResourceOperationDao")
@GradfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class RoleResourceOperationDaoImpl extends AbstractBaseDao<RoleResourceOperation, RoleResourceOperationQuery> implements IRoleResourceOperationDao {

}
