package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.framework.cache.annotation.CrdfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import liangchen.wang.gradf.component.foura.dao.IOperationDao;
import liangchen.wang.gradf.component.foura.dao.entity.Operation;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-12 23:47:02
*/
@Repository("Crdf_Foura_DefaultOperationDao")
@CrdfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class OperationDaoImpl extends AbstractBaseDao<Operation> implements IOperationDao {

}
