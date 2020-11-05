package liangchen.wang.gradf.framework.commons.utils;

import com.google.common.collect.Queues;

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
    private final Consumer<List<E>> consumer;
    private List<E> buffer;

    public BatchProcessor(int batchSize, Consumer<List<E>> consumer) {
        this.batchSize = batchSize;
        this.blockingQueue = new ArrayBlockingQueue<>(batchSize);
        this.consumer = consumer;
        this.buffer = new ArrayList<>(batchSize);
    }

    public void drain(E e) {
        buffer.add(e);
        if (buffer.size() >= batchSize) {
            consumer.accept(buffer);
            buffer = new ArrayList<>(batchSize);
        }
    }

    public void flush() {
        if (buffer.size() > 0) {
            consumer.accept(buffer);
        }
    }

    public void put(E e) {
        try {
            blockingQueue.put(e);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void onDrain(Consumer<List<E>> consumer) {
        try {
            while (true) {
                buffer = new ArrayList<>(batchSize);
                Queues.drain(blockingQueue, buffer, batchSize, 5, TimeUnit.SECONDS);
                if (buffer.size() == 0) {
                    break;
                }
                consumer.accept(buffer);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
