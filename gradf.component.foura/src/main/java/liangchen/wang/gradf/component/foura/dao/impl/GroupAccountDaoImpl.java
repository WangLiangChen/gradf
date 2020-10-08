package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IGroupAccountDao;
import liangchen.wang.gradf.component.foura.dao.entity.GroupAccount;
import liangchen.wang.gradf.component.foura.dao.query.GroupAccountQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Repository("Gradf_Foura_DefaultGroupAccountDao")
@GradfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class GroupAccountDaoImpl extends AbstractBaseDao<GroupAccount, GroupAccountQuery> implements IGroupAccountDao {

}
