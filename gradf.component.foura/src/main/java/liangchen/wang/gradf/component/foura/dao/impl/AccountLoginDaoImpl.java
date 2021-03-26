package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IAccountLoginDao;
import liangchen.wang.gradf.component.foura.dao.entity.AccountLogin;
import liangchen.wang.gradf.component.foura.dao.query.AccountLoginQuery;

import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-07-21 22:30:15
 */
@Repository("Gradf_Foura_DefaultAccountLoginDao")

public class AccountLoginDaoImpl extends AbstractJdbcDao<AccountLogin, AccountLoginQuery> implements IAccountLoginDao {

}
