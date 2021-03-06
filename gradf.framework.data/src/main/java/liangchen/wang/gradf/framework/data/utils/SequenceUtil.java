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
    private final ISequenceManager sequenceManager = BeanLoader.INSTANCE.getBean("Gradf_Data_SequenceManager");

    public Long sequenceNumber(String sequenceKey) {
        return sequenceManager.sequenceNumber(sequenceKey);
    }

    public Long sequenceNumber(String sequenceKey, long initialValue) {
        return sequenceManager.sequenceNumber(sequenceKey, initialValue);
    }
}
