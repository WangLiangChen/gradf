package liangchen.wang.gradf.framework.commons.snowflake.generator;


import liangchen.wang.crdf.framework.commons.validator.Assert;
import liangchen.wang.crdf.framework.commons.validator.AssertLevel;

public class BitsAllocator {
    /**
     * Total 64 bits
     */
    public static final int TOTAL_BITS = 1 << 6;

    /**
     * Bits for [sign-> second-> workId-> sequence]
     */
    private final int signBits = 1;
    private final int timestampBits;
    private final int workerIdBits;
    private final int sequenceBits;

    /**
     * Max value for workId & sequence
     */
    private final long maxDeltaTime;
    private final long maxWorkerId;
    private final long maxSequence;

    /**
     * Shift for timestamp & workerId
     */
    private final int timestampShift;
    private final int workerIdShift;

 
    public BitsAllocator(int timestampBits, int workerIdBits, int sequenceBits) {
        // make sure allocated 64 bits
        int allocateTotalBits = signBits + timestampBits + workerIdBits + sequenceBits;
        Assert.INSTANCE.isTrue(allocateTotalBits == TOTAL_BITS, AssertLevel.INFO, "allocate not enough 64 bits");

        // initialize bits
        this.timestampBits = timestampBits;
        this.workerIdBits = workerIdBits;
		this.sequenceBits = sequenceBits;

        // initialize max value
        this.maxDeltaTime = ~(-1L << timestampBits);
        this.maxWorkerId = ~(-1L << workerIdBits);
        this.maxSequence = ~(-1L << sequenceBits);

        // initialize shift
        this.timestampShift = workerIdBits + sequenceBits;
        this.workerIdShift = sequenceBits;
    }

    /**
     * Allocate bits for UID according to delta seconds & workerId & sequence<br>
     * <b>Note that: </b>The highest bit will always be 0 for sign
     * 
     * @param deltaSeconds
     * @param workerId
     * @param sequence
     * @return
     */
    public long allocate(long deltaSeconds, long workerId, long sequence) {
        return (deltaSeconds << timestampShift) | (workerId << workerIdShift) | sequence;
    }
    
    /**
     * Getters
     */
    public int getSignBits() {
        return signBits;
    }

    public int getTimestampBits() {
        return timestampBits;
    }

    public int getWorkerIdBits() {
        return workerIdBits;
    }

    public int getSequenceBits() {
        return sequenceBits;
    }

    public long getMaxDeltaTime() {
        return maxDeltaTime;
    }

    public long getMaxWorkerId() {
        return maxWorkerId;
    }

    public long getMaxSequence() {
        return maxSequence;
    }

    public int getTimestampShift() {
        return timestampShift;
    }

    public int getWorkerIdShift() {
        return workerIdShift;
    }
        
}
