package liangchen.wang.gradf.framework.commons.snowflake.factory;


import liangchen.wang.crdf.framework.commons.snowflake.generator.DefaultGenerator;
import liangchen.wang.crdf.framework.commons.snowflake.generator.IGenerator;
import liangchen.wang.crdf.framework.commons.snowflake.worker.IWorkerIdAssigner;

import java.util.concurrent.TimeUnit;

public class DefaultFactory implements IFactory {

	protected int timeBits;
	protected int workerBits;
	protected int seqBits;

	protected long epochTime;
	protected TimeUnit timeUnit;

	@Override
	public IGenerator create(IWorkerIdAssigner workerIdAssigner) {
		return create(workerIdAssigner.workerId());
	}

	@Override
	public IGenerator create(Long workerId) {
		return new DefaultGenerator(timeBits, workerBits, seqBits, epochTime, workerId, timeUnit);
	}

	public void setTimeBits(int timeBits) {
		this.timeBits = timeBits;
	}

	public void setWorkerBits(int workerBits) {
		this.workerBits = workerBits;
	}

	public void setSeqBits(int seqBits) {
		this.seqBits = seqBits;
	}

	public void setEpochTime(long epochTime) {
		this.epochTime = epochTime;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

}
