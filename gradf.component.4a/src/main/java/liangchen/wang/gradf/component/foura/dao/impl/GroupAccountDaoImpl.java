package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.framework.cache.annotation.CrdfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import liangchen.wang.gradf.component.foura.dao.IGroupAccountDao;
import liangchen.wang.gradf.component.foura.dao.entity.GroupAccount;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
*/
@Repository("Crdf_Foura_DefaultGroupAccountDao")
@CrdfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class GroupAccountDaoImpl extends AbstractBaseDao<GroupAccount> implements IGroupAccountDao {

}
