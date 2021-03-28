package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IUrlDao;
import liangchen.wang.gradf.component.foura.dao.entity.Url;
import liangchen.wang.gradf.component.foura.dao.query.UrlQuery;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

/**
 * @author LiangChen.Wang 2020-04-13 00:40:41
 */
@Repository("Gradf_Foura_DefaultUrlDao")

public class UrlDaoImpl extends AbstractJdbcDao<Url, UrlQuery> implements IUrlDao {

}
