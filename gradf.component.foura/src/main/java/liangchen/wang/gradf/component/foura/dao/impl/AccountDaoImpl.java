package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IAccountDao;
import liangchen.wang.gradf.component.foura.dao.entity.Account;
import liangchen.wang.gradf.component.foura.dao.query.AccountQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Repository("Gradf_Foura_DefaultAccountDao")
@GradfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, excludeMethods = {"accountIdByaccountKey"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class AccountDaoImpl extends AbstractBaseDao<Account, AccountQuery> implements IAccountDao {

}
