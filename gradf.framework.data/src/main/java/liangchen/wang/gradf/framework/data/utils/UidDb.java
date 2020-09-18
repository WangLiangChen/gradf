package liangchen.wang.gradf.framework.data.utils;

import liangchen.wang.gradf.framework.commons.snowflake.factory.IUIDGeneratorFactory;
import liangchen.wang.gradf.framework.commons.snowflake.factory.SecondTimeUIDGeneratorFactory;
import liangchen.wang.gradf.framework.commons.snowflake.generator.IUIDGenerator;

/**
 * @author LiangChen.Wang 2019/10/31 20:29
 */
public enum UidDb {
    /**
     *
     */
    INSTANCE;
    private final String UID = "UID";
    private IUIDGenerator distributedGenerator;

    UidDb() {
        IUIDGeneratorFactory factory = new SecondTimeUIDGeneratorFactory();
        Long sequenceNumber = SequenceUtil.INSTANCE.sequenceNumber(UID);
        distributedGenerator = factory.create(sequenceNumber);
    }

    public Long uid() {
        return distributedGenerator.getUID();
    }
}
