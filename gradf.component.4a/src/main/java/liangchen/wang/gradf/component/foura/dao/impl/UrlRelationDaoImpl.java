package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IUrlRelationDao;
import liangchen.wang.gradf.component.foura.dao.entity.UrlRelation;
import liangchen.wang.gradf.framework.cache.annotation.CrdfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-07-29 13:56:01
*/
@Repository("Crdf_Foura_DefaultUrlRelationDao")
@CrdfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class UrlRelationDaoImpl extends AbstractBaseDao<UrlRelation> implements IUrlRelationDao {

}
