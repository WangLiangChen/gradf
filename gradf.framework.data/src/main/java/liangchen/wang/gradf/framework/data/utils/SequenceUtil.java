package liangchen.wang.gradf.framework.data.utils;


import liangchen.wang.gradf.framework.data.manager.ISequenceManager;
import liangchen.wang.gradf.framework.springboot.context.BeanLoader;

/**
 * @author LiangChen.Wang
 */
public enum SequenceUtil {
    /**
     *
     */
    INSTANCE;
    private final ISequenceManager sequenceManager = BeanLoader.getBean("Crdf_Data_SequenceManager");

    public Long sequenceNumber(String sequenceKey) {
        return sequenceManager.sequenceNumber(sequenceKey);
    }

    public Long sequenceNumber(String sequenceKey, int initialValue) {
        return sequenceManager.sequenceNumber(sequenceKey, initialValue);
    }
}
