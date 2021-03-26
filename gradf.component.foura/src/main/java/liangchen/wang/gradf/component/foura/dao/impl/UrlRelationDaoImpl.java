package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IUrlRelationDao;
import liangchen.wang.gradf.component.foura.dao.entity.UrlRelation;
import liangchen.wang.gradf.component.foura.dao.query.UrlRelationQuery;

import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-07-29 13:56:01
 */
@Repository("Gradf_Foura_DefaultUrlRelationDao")

public class UrlRelationDaoImpl extends AbstractJdbcDao<UrlRelation, UrlRelationQuery> implements IUrlRelationDao {

}
