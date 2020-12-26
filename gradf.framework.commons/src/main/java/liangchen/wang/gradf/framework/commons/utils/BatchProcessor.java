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
    private final int batchSize;
    private final long timeout;
    private final TimeUnit timeUnit;
    private Consumer<List<E>> consumer;
    private Runnable finishRunable;
    private List<E> bufferList;
    private boolean finished = false;

    public BatchProcessor(int batchSize) {
        this(batchSize, 5, TimeUnit.SECONDS);
    }

    public BatchProcessor(int batchSize, long timeout, TimeUnit timeUnit) {
        this.batchSize = batchSize;
        this.blockingQueue = new ArrayBlockingQueue<>(batchSize);
        this.bufferList = new ArrayList<>(batchSize);
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
            blockingQueue.put(e);
        } catch (InterruptedException ex) {
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
        ThreadPoolUtil.INSTANCE.getExecutorService().execute(() -> {
            while (true) {
                bufferList = new ArrayList<>(batchSize);
                try {
                    Queues.drain(blockingQueue, bufferList, batchSize, timeout, timeUnit);
                } catch (InterruptedException e) {
                    throw new ErrorException(e);
                }
                if (bufferList.size() == 0) {
                    finished = true;
                    if (null != finishRunable) {
                        finishRunable.run();
                    }
                    break;
                }
                if (null == consumer) {
                    break;
                }
                consumer.accept(bufferList);
            }
        });
    }
}
