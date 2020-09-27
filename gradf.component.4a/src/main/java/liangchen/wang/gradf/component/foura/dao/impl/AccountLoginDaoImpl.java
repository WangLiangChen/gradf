package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.framework.cache.annotation.CrdfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import liangchen.wang.gradf.component.foura.dao.IAccountLoginDao;
import liangchen.wang.gradf.component.foura.dao.entity.AccountLogin;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-07-21 22:30:15
*/
@Repository("Crdf_Foura_DefaultAccountLoginDao")
@CrdfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class AccountLoginDaoImpl extends AbstractBaseDao<AccountLogin> implements IAccountLoginDao {

}
