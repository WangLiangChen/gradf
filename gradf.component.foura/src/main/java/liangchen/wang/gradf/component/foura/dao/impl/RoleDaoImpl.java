package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IRoleDao;
import liangchen.wang.gradf.component.foura.dao.entity.Role;
import liangchen.wang.gradf.component.foura.dao.query.RoleQuery;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Repository("Gradf_Foura_DefaultRoleDao")

public class RoleDaoImpl extends AbstractJdbcDao<Role, RoleQuery> implements IRoleDao {

}
