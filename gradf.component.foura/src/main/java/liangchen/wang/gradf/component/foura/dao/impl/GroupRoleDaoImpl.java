package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IGroupRoleDao;
import liangchen.wang.gradf.component.foura.dao.entity.GroupRole;
import liangchen.wang.gradf.component.foura.dao.query.GroupRoleQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-10-08 19:11:17
*/
@Repository("Gradf_Foura_DefaultGroupRoleDao")
@GradfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class GroupRoleDaoImpl extends AbstractJdbcDao<GroupRole,GroupRoleQuery> implements IGroupRoleDao {

}
