package liangchen.wang.gradf.component.business.dao;

import liangchen.wang.gradf.component.business.dao.entity.Infinite;
import liangchen.wang.gradf.component.business.dao.query.InfiniteQuery;
import liangchen.wang.gradf.framework.data.base.IAbstractDao;

/**
 * @author LiangChen.Wang 2020-05-12 19:37:27
 */
public interface IInfiniteDao extends IAbstractDao<Infinite, InfiniteQuery> {

    void insertChild(Infinite infinite);

    void delete(Long infinite_id);
}
