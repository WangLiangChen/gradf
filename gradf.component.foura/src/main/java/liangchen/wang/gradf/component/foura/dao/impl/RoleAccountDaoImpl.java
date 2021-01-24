package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IRoleAccountDao;
import liangchen.wang.gradf.component.foura.dao.entity.RoleAccount;
import liangchen.wang.gradf.component.foura.dao.query.RoleAccountQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Repository("Gradf_Foura_DefaultRoleAccountDao")
@GradfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class RoleAccountDaoImpl extends AbstractJdbcDao<RoleAccount, RoleAccountQuery> implements IRoleAccountDao {

}
