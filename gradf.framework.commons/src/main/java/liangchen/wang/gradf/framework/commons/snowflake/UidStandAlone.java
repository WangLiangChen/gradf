package liangchen.wang.gradf.framework.commons.snowflake;

import liangchen.wang.crdf.framework.commons.snowflake.factory.IFactory;
import liangchen.wang.crdf.framework.commons.snowflake.factory.SecondTimeFactory;
import liangchen.wang.crdf.framework.commons.snowflake.generator.IGenerator;

/**
 * @author LiangChen.Wang 2019/10/31 11:30
 */
public enum UidStandAlone {
    INSTANCE;
    private IGenerator simpleGenerator;
    UidStandAlone(){
        IFactory factory = new SecondTimeFactory();
        simpleGenerator  = factory.create(4000000L);
    }
    public Long uid() {
        return simpleGenerator.getUID();
    }
}
