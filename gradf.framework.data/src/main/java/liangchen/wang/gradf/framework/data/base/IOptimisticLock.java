package liangchen.wang.gradf.framework.data.base;

/**
 * @author LiangChen.Wang
 */
public interface IOptimisticLock {

    Long getVersion();

    void setVersion(Long version);
}
