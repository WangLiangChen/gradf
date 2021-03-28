package liangchen.wang.gradf.component.commons.dao.impl;

import liangchen.wang.gradf.component.commons.dao.IExtendPropertyDao;
import liangchen.wang.gradf.component.commons.dao.entity.ExtendProperty;
import liangchen.wang.gradf.component.commons.dao.query.ExtendPropertyQuery;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

/**
 * @author LiangChen.Wang 2019-12-29 20:32:33
 */
@Repository("Gradf_Business_DefaultExtendPropertyDao")
public class ExtendPropertyDaoImpl extends AbstractJdbcDao<ExtendProperty, ExtendPropertyQuery> implements IExtendPropertyDao {

}
