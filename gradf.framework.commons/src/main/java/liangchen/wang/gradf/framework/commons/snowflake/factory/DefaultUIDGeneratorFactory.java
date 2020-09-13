package liangchen.wang.gradf.framework.commons.snowflake.factory;

import liangchen.wang.gradf.framework.commons.snowflake.generator.DefaultUIDGenerator;
import liangchen.wang.gradf.framework.commons.snowflake.generator.IUIDGenerator;
import liangchen.wang.gradf.framework.commons.snowflake.worker.IWorkerIdBuilder;

import java.util.concurrent.TimeUnit;

/**
 * use DefaultUIDGenerator
 *
 * @author LiangChen.Wang
 */
public class DefaultUIDGeneratorFactory implements IUIDGeneratorFactory {

    protected int timeBits;
    protected int workerBits;
    protected int seqBits;

    protected long epochTime;
    protected TimeUnit timeUnit;

    @Override
    public IUIDGenerator create(IWorkerIdBuilder workerIdAssigner) {
        return create(workerIdAssigner.workerId());
    }

    @Override
    public IUIDGenerator create(Long workerId) {
        return new DefaultUIDGenerator(timeBits, workerBits, seqBits, epochTime, timeUnit, workerId);
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
