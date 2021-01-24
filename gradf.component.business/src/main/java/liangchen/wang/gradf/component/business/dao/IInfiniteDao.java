package liangchen.wang.gradf.component.business.dao;

import liangchen.wang.gradf.component.business.dao.entity.Infinite;
import liangchen.wang.gradf.component.business.dao.query.InfiniteQuery;
import liangchen.wang.gradf.framework.data.core.IDao;

/**
 * @author LiangChen.Wang 2020-05-12 19:37:27
 */
public interface IInfiniteDao extends IDao<Infinite, InfiniteQuery> {

    void insertChild(Infinite infinite);

    void delete(Long infinite_id);
}
