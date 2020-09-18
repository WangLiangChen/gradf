package liangchen.wang.gradf.framework.data.manager;

/**
 * @author LiangChen.Wang
 */
public interface ISequenceManager {
    Long sequenceNumber(String sequenceKey, int initialValue);

    Long sequenceNumber(String sequenceKey);
}
