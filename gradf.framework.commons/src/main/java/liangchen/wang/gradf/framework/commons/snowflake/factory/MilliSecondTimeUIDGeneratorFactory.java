package liangchen.wang.gradf.framework.commons.snowflake.factory;

import java.util.concurrent.TimeUnit;

/*
 * <pre>{@code
 * +------+----------------------+----------------+-----------+
 * | sign |  delta millisecond   | worker node id | sequence  |
 * +------+----------------------+----------------+-----------+
 *   1bit          41bits              10bits         12bits
 * }</pre>
 */
public class MilliSecondTimeUIDGeneratorFactory extends DefaultUIDGeneratorFactory {
	public MilliSecondTimeUIDGeneratorFactory() {
		super.timeBits = 41;
		super.workerBits = 10;
		super.seqBits = 12;
		// epoch millisecond,default: 2018-01-01 (millisecond:1514764800000L)
		super.epochTime = 1514764800000L;
		super.timeUnit = TimeUnit.MILLISECONDS;
	}
}
