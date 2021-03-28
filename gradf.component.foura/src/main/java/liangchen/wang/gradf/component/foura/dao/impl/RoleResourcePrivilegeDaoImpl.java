package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IRoleResourcePrivilegeDao;
import liangchen.wang.gradf.component.foura.dao.entity.RoleResourcePrivilege;
import liangchen.wang.gradf.component.foura.dao.query.RoleResourcePrivilegeQuery;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

/**
 * @author LiangChen.Wang 2020-04-12 23:55:23
 */
@Repository("Gradf_Foura_DefaultRoleResourcePrivilegeDao")

public class RoleResourcePrivilegeDaoImpl extends AbstractJdbcDao<RoleResourcePrivilege, RoleResourcePrivilegeQuery> implements IRoleResourcePrivilegeDao {

}
