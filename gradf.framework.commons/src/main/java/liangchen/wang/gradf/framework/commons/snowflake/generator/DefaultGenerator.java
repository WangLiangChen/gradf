package liangchen.wang.gradf.framework.commons.snowflake.generator;

import liangchen.wang.crdf.framework.commons.exeception.InfoException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 */
public class DefaultGenerator implements IGenerator {
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	protected BitsAllocator bitsAllocator;
	protected long workerId;
	protected long epochTime;
	protected TimeUnit timeUnit;

	/** Volatile fields caused by nextId() */
	protected long sequence = 0L;
	protected long lastTime = -1L;

	public DefaultGenerator(int timeBits, int workerBits, int seqBits, long epochTime, long workerId,TimeUnit timeUnit) {		
		this.workerId=workerId;
		this.epochTime=epochTime;
		this.timeUnit = timeUnit;		
		bitsAllocator = new BitsAllocator(timeBits, workerBits, seqBits);
		if (workerId > bitsAllocator.getMaxWorkerId()) {
			throw new InfoException("Worker id:{} exceeds the max worker id:{}",workerId, bitsAllocator.getMaxWorkerId());
		}			
		long currentTime = getCurrentTime();
		if (currentTime - epochTime > bitsAllocator.getMaxDeltaTime()) {
			throw new InfoException("epoch timestamp:{} exceeds the max time:{} ",epochTime, bitsAllocator.getMaxDeltaTime());
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

		Date thatTime = new Date(timeUnit.toMillis(this.epochTime + deltaTime));
		String thatTimeStr = simpleDateFormat.format(thatTime);

		// format as string
		return String.format("{\"UID\":\"%d\",\"timestamp\":\"%s\",\"workerId\":\"%d\",\"sequence\":\"%d\"}", uid, thatTimeStr, workerId, sequence);
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
	private long getCurrentTime() {
		long currentTime = timeUnit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		if (currentTime - epochTime > bitsAllocator.getMaxDeltaTime()) {
			throw new InfoException("Timestamp bits is exhausted. Refusing UID generate. Now:{} " , currentTime);
		}
		return currentTime;
	}

}
