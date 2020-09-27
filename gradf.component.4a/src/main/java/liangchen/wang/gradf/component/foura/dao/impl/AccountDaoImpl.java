package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.framework.cache.annotation.CrdfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import liangchen.wang.gradf.component.foura.dao.IAccountDao;
import liangchen.wang.gradf.component.foura.dao.entity.Account;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Repository("Crdf_Foura_DefaultAccountDao")
@CrdfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, excludeMethods = {"accountIdByaccountKey"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class AccountDaoImpl extends AbstractBaseDao<Account> implements IAccountDao {

}
