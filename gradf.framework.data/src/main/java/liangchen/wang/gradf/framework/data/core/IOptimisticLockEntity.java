package liangchen.wang.gradf.framework.data.core;

/**
 * @author LiangChen.Wang
 */
public interface IOptimisticLockEntity {

    Long getVersion();

    void setVersion(Long version);
}
