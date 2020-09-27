package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.framework.cache.annotation.CrdfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import liangchen.wang.gradf.component.foura.dao.IGroupDao;
import liangchen.wang.gradf.component.foura.dao.entity.Group;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-12 01:44:04
*/
@Repository("Crdf_Foura_DefaultGroupDao")
@CrdfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class GroupDaoImpl extends AbstractBaseDao<Group> implements IGroupDao {

}
