package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IOperationLogDao;
import liangchen.wang.gradf.component.foura.dao.entity.OperationLog;
import liangchen.wang.gradf.component.foura.dao.query.OperationLogQuery;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

/**
 * @author LiangChen.Wang 2020-03-07 10:24:36
 */
@Repository("Gradf_Business_DefaultOperationLogDao")
public class OperationLogDaoImpl extends AbstractJdbcDao<OperationLog, OperationLogQuery> implements IOperationLogDao {

}
