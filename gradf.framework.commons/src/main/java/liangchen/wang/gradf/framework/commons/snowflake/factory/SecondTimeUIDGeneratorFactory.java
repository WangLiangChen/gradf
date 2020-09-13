package liangchen.wang.gradf.framework.commons.snowflake.factory;

import java.util.concurrent.TimeUnit;

/*
 * <pre>{@code
 * +------+----------------------+----------------+-----------+
 * | sign |  delta millisecond   | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          28bits              22bits         13bits
 * }</pre>
 */
public class SecondTimeUIDGeneratorFactory extends DefaultUIDGeneratorFactory {
    public SecondTimeUIDGeneratorFactory() {
        super.timeBits = 28;
        super.workerBits = 22;
        super.seqBits = 13;
        // epoch seconds,default: 2018-01-01 (seconds:1514764800L)
        super.epochTime = 1514764800L;
        super.timeUnit = TimeUnit.SECONDS;
    }
}
