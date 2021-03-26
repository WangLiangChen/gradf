package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IRoleResourceOperationDao;
import liangchen.wang.gradf.component.foura.dao.entity.RoleResourceOperation;
import liangchen.wang.gradf.component.foura.dao.query.RoleResourceOperationQuery;

import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-12 23:55:49
 */
@Repository("Gradf_Foura_DefaultRoleResourceOperationDao")

public class RoleResourceOperationDaoImpl extends AbstractJdbcDao<RoleResourceOperation, RoleResourceOperationQuery> implements IRoleResourceOperationDao {

}
