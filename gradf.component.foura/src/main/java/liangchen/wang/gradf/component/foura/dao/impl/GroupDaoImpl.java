package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IGroupDao;
import liangchen.wang.gradf.component.foura.dao.entity.Group;
import liangchen.wang.gradf.component.foura.dao.query.GroupQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-12 01:44:04
 */
@Repository("Gradf_Foura_DefaultGroupDao")
@GradfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class GroupDaoImpl extends AbstractBaseDao<Group, GroupQuery> implements IGroupDao {

}
