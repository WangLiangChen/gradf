package liangchen.wang.gradf.framework.data.dao;

/**
 * @author LiangChen.Wang
 */
public interface ISequenceDao {
    Long sequenceNumber(String sequenceKey, long initialValue);
}
