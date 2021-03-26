package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IOperationDao;
import liangchen.wang.gradf.component.foura.dao.entity.Operation;
import liangchen.wang.gradf.component.foura.dao.query.OperationQuery;

import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-12 23:47:02
 */
@Repository("Gradf_Foura_DefaultOperationDao")

public class OperationDaoImpl extends AbstractJdbcDao<Operation, OperationQuery> implements IOperationDao {

}
