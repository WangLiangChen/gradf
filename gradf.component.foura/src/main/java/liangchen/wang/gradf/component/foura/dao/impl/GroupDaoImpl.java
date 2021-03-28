package liangchen.wang.gradf.component.foura.dao.impl;

import liangchen.wang.gradf.component.foura.dao.IGroupDao;
import liangchen.wang.gradf.component.foura.dao.entity.Group;
import liangchen.wang.gradf.component.foura.dao.query.GroupQuery;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

/**
 * @author LiangChen.Wang 2020-04-12 01:44:04
 */
@Repository("Gradf_Foura_DefaultGroupDao")

public class GroupDaoImpl extends AbstractJdbcDao<Group, GroupQuery> implements IGroupDao {

}
