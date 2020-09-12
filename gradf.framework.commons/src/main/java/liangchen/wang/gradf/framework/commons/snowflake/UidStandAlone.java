package liangchen.wang.gradf.framework.commons.snowflake;

import liangchen.wang.gradf.framework.commons.snowflake.factory.IUIDGeneratorFactory;
import liangchen.wang.gradf.framework.commons.snowflake.factory.SecondTimeUIDGeneratorFactory;
import liangchen.wang.gradf.framework.commons.snowflake.generator.IUIDGenerator;

/**
 * @author LiangChen.Wang 2019/10/31 11:30
 */
public enum UidStandAlone {
    /**
     * instance
     */
    INSTANCE;
    private IUIDGenerator simpleGenerator;

    UidStandAlone() {
        IUIDGeneratorFactory factory = new SecondTimeUIDGeneratorFactory();
        simpleGenerator = factory.create(4000000L);
    }

    public Long uid() {
        return simpleGenerator.getUID();
    }
}
