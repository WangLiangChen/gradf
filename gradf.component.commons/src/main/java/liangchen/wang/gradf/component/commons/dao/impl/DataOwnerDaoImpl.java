package liangchen.wang.gradf.component.commons.dao.impl;

import liangchen.wang.gradf.component.commons.dao.IDataOwnerDao;
import liangchen.wang.gradf.component.commons.dao.entity.DataOwner;
import liangchen.wang.gradf.component.commons.dao.query.DataOwnerQuery;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

/**
 * @author LiangChen.Wang 2019-12-29 20:29:37
 */
@Repository("Gradf_Business_DefaultDataOwnerDao")
public class DataOwnerDaoImpl extends AbstractJdbcDao<DataOwner, DataOwnerQuery> implements IDataOwnerDao {

}
