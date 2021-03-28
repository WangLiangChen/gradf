package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IResourceDao;
import liangchen.wang.gradf.component.foura.dao.entity.Resource;
import liangchen.wang.gradf.component.foura.dao.query.ResourceQuery;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Repository("Gradf_Foura_DefaultResourceDao")

public class ResourceDaoImpl extends AbstractJdbcDao<Resource, ResourceQuery> implements IResourceDao {

}
