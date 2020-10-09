package liangchen.wang.gradf.component.business.manager;

import liangchen.wang.gradf.component.business.manager.domain.parameter.InfiniteParameterDomain;

import javax.transaction.Transactional;

/**
 * @author LiangChen.Wang 2020-05-12 19:37:27
 */
public interface IInfiniteManager {

    Long insertRoot(InfiniteParameterDomain parameter);

    Long insertChild(InfiniteParameterDomain parameter);

    @Transactional
    void delete(Long infinite_id);
}
