package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.framework.cache.annotation.CrdfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import liangchen.wang.gradf.component.foura.dao.IUrlDao;
import liangchen.wang.gradf.component.foura.dao.entity.Url;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-04-13 00:40:41
*/
@Repository("Crdf_Foura_DefaultUrlDao")
@CrdfAutoCacheable(clearMethods = {"insert*", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class UrlDaoImpl extends AbstractBaseDao<Url> implements IUrlDao {

}
