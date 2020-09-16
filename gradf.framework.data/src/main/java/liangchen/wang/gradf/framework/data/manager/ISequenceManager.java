package liangchen.wang.gradf.framework.data.manager;

public interface ISequenceManager {
    Long sequenceNumber(String sequenceKey, int initialValue);

    Long sequenceNumber(String sequenceKey);
}
