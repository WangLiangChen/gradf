package liangchen.wang.gradf.framework.commons.utils;

import com.google.common.collect.Queues;
import liangchen.wang.gradf.framework.commons.exception.ErrorException;
import liangchen.wang.gradf.framework.commons.exception.InfoException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author LiangChen.Wang 2020/11/5
 */
public class BatchProcessor<E> {
    private final BlockingQueue<E> blockingQueue;
    private final List<E> bufferList;
    private final int batchSize;
    private final long timeout;
    private final TimeUnit timeUnit;
    private Consumer<List<E>> consumer;
    private Runnable finishRunable;
    private boolean finished = false;

    public BatchProcessor(int batchSize) {
        this(batchSize, 5, TimeUnit.SECONDS);
    }

    public BatchProcessor(int batchSize, long timeout, TimeUnit timeUnit) {
        this.batchSize = batchSize;
        this.blockingQueue = new ArrayBlockingQueue<>(batchSize * 4);
        this.bufferList = new ArrayList<>(batchSize * 2);
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    /*public void drain(E e) {
        if (null == consumer) {
            return;
        }
        buffer.add(e);
        if (buffer.size() >= batchSize) {
            consumer.accept(buffer);
            buffer = new ArrayList<>(batchSize);
        }
    }

    public void flush() {
        if (null == consumer || null == finishRunable) {
            return;
        }
        if (buffer.size() > 0) {
            consumer.accept(buffer);
        }
        finishRunable.run();
    }*/

    public boolean put(E e) {
        if (finished) {
            return false;
        }
        if (null == consumer) {
            throw new InfoException("Set Consumer by method onConsume");
        }
        try {
            blockingQueue.offer(e, timeout, timeUnit);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ex);
        }
        return true;
    }

    public void onConsume(Consumer<List<E>> consumer) {
        this.consumer = consumer;
        onDrain();
    }

    public void onFinish(Runnable finishRunnable) {
        this.finishRunable = finishRunnable;
    }

    private void onDrain() {
        ThreadPoolUtil.INSTANCE.getExecutor().execute(() -> {
            while (true) {
                bufferList.clear();
                try {
                    Queues.drain(blockingQueue, bufferList, batchSize, timeout, timeUnit);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ErrorException(e);
                }
                int size = bufferList.size();
                if (size == batchSize) {
                    if (null == consumer) {
                        break;
                    }
                    consumer.accept(bufferList);
                    continue;
                }
                finished = true;
                if (size > 0) {
                    consumer.accept(bufferList);
                }
                if (null != finishRunable) {
                    finishRunable.run();
                }
                break;
            }
        });
    }
}
