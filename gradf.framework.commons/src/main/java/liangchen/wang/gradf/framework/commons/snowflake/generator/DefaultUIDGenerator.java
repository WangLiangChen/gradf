package liangchen.wang.gradf.framework.commons.snowflake.generator;


import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.DateTimeUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 */
public class DefaultUIDGenerator implements IUIDGenerator {
    protected BitsAllocator bitsAllocator;
    protected long workerId;
    protected long epochTime;
    protected TimeUnit timeUnit;

    /**
     * Volatile fields caused by nextId()
     */
    protected long sequence = 0L;
    protected long lastTime = -1L;

    /**
     * @param timeBits   时间位
     * @param workerBits 节点位
     * @param seqBits    序列位
     * @param epochTime  时间
     * @param timeUnit   时间单位
     * @param workerId   节点号
     */
    public DefaultUIDGenerator(int timeBits, int workerBits, int seqBits, long epochTime, TimeUnit timeUnit, long workerId) {
        this.epochTime = epochTime;
        this.timeUnit = timeUnit;
        this.workerId = workerId;
        bitsAllocator = new BitsAllocator(timeBits, workerBits, seqBits);
        long maxWorkerId = bitsAllocator.getMaxWorkerId();
        if (workerId > maxWorkerId) {
            throw new InfoException("Worker id:{} exceeds the max worker id:{}", workerId, maxWorkerId);
        }
    }

    @Override
    public Long getUID() {
        return nextId();
    }

    @Override
    public String parseUID(Long uid) {
        long totalBits = BitsAllocator.TOTAL_BITS;
        long signBits = bitsAllocator.getSignBits();
        long timestampBits = bitsAllocator.getTimestampBits();
        long workerIdBits = bitsAllocator.getWorkerIdBits();
        long sequenceBits = bitsAllocator.getSequenceBits();

        // parse UID
        long sequence = (uid << (totalBits - sequenceBits)) >>> (totalBits - sequenceBits);
        long workerId = (uid << (timestampBits + signBits)) >>> (totalBits - workerIdBits);
        long deltaTime = uid >>> (workerIdBits + sequenceBits);
        String timeString = DateTimeUtil.INSTANCE.getYYYY_MM_DD_HH_MM_SS_SSS(timeUnit.toMillis(this.epochTime + deltaTime));
        // format as string
        return String.format("{\"UID\":\"%d\",\"timestamp\":\"%s\",\"workerId\":\"%d\",\"sequence\":\"%d\"}", uid, timeString, workerId, sequence);
    }

    protected synchronized long nextId() {
        long currentTime = getCurrentTime();
        // Clock moved backwards, refuse to generate uid
        if (currentTime < lastTime) {
            long refusedTime = lastTime - currentTime;
            throw new InfoException("Clock moved backwards. Refusing for {} seconds", refusedTime);
        }

        if (currentTime == lastTime) {
            sequence = (sequence + 1) & bitsAllocator.getMaxSequence();
            // Exceed the max sequence, we wait the next second to generate uid
            if (sequence == 0) {
                currentTime = getNextTime(lastTime);
            }
        } else {
            sequence = 0L;
        }
        lastTime = currentTime;

        return bitsAllocator.allocate(currentTime - epochTime, workerId, sequence);
    }

    private long getNextTime(long lastTime) {
        long nextTime = getCurrentTime();
        while (nextTime <= lastTime) {
            nextTime = getCurrentTime();
        }
        return nextTime;
    }

    /**
     * @return timeUnit单位的当前时间
     */
    private long getCurrentTime() {
        // 转换当前毫秒到timeUnit单位的时间
        long currentTime = timeUnit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        long maxDeltaTime = bitsAllocator.getMaxDeltaTime();
        if (currentTime - epochTime > maxDeltaTime) {
            throw new InfoException("Timestamp bits is exhausted. Now:{},epochTime:{},max:{}", currentTime, epochTime, maxDeltaTime);
        }
        return currentTime;
    }

}
