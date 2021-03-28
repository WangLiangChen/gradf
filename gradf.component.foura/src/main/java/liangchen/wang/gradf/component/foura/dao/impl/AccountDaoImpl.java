package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IAccountDao;
import liangchen.wang.gradf.component.foura.dao.entity.Account;
import liangchen.wang.gradf.component.foura.dao.query.AccountQuery;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Repository("Gradf_Foura_DefaultAccountDao")
public class AccountDaoImpl extends AbstractJdbcDao<Account, AccountQuery> implements IAccountDao {

}
