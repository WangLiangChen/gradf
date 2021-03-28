package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IGroupAccountDao;
import liangchen.wang.gradf.component.foura.dao.entity.GroupAccount;
import liangchen.wang.gradf.component.foura.dao.query.GroupAccountQuery;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Repository("Gradf_Foura_DefaultGroupAccountDao")

public class GroupAccountDaoImpl extends AbstractJdbcDao<GroupAccount, GroupAccountQuery> implements IGroupAccountDao {

}
