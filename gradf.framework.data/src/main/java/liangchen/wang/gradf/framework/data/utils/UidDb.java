package liangchen.wang.gradf.framework.data.utils;

import liangchen.wang.crdf.framework.commons.snowflake.factory.IFactory;
import liangchen.wang.crdf.framework.commons.snowflake.factory.SecondTimeFactory;
import liangchen.wang.crdf.framework.commons.snowflake.generator.IGenerator;

/**
 * @author LiangChen.Wang 2019/10/31 20:29
 */
public enum UidDb {
    INSTANCE;
    private final String UID = "UID";
    private IGenerator distributedGenerator;

    UidDb() {
        IFactory factory = new SecondTimeFactory();
        Long sequenceNumber = SequenceUtil.INSTANCE.sequenceNumber(UID);
        distributedGenerator = factory.create(sequenceNumber);
    }

    public Long uid() {
        return distributedGenerator.getUID();
    }
}
