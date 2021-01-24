package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IRoleResourcePrivilegeDao;
import liangchen.wang.gradf.component.foura.dao.entity.RoleResourcePrivilege;
import liangchen.wang.gradf.component.foura.dao.query.RoleResourcePrivilegeQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-12 23:55:23
 */
@Repository("Gradf_Foura_DefaultRoleResourcePrivilegeDao")
@GradfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class RoleResourcePrivilegeDaoImpl extends AbstractJdbcDao<RoleResourcePrivilege, RoleResourcePrivilegeQuery> implements IRoleResourcePrivilegeDao {

}
