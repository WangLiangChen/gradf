package liangchen.wang.gradf.framework.commons.utils;

import liangchen.wang.gradf.framework.commons.exception.ErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author LiangChen.Wang 2019/10/25 14:28
 */
public enum ConcurrentUtil {
    /**
     *
     */
    INSTANCE;

    /**
     * convert a list of CompletableFuture to a CompletableFuture of a result list,
     * so we can process it later when all Stages is completed
     *
     * @param futures
     * @param <T>     type of result
     * @return CompletableFuture of a list
     */

    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<List<T>> completableFuture2List(Collection<CompletableFuture<T>> futures) {
        CompletableFuture<T>[] completableFutures = futures.toArray(new CompletableFuture[futures.size()]);
        return CompletableFuture.allOf(completableFutures).thenApply(e -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    /**
     * convert a list of CompletableFuture to a CompletableFuture of a result list,
     * so we can process it later when all Stages is completed
     *
     * @param futures
     * @param <T>     type of result
     * @return CompletableFuture of a list
     */
    public <T> CompletableFuture<List<T>> completableFuture2List(CompletableFuture<T>[] futures) {
        ArrayList<CompletableFuture<T>> completableFutures = new ArrayList<>(Arrays.asList(futures));
        return CompletableFuture.allOf(futures).thenApply(e -> completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

    public void block() {
        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ErrorException(e);
        }
    }

    public void threadSleep(long timeout, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ErrorException(e);
        }
    }

    public void threadJoin(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            thread.interrupt();
            throw new ErrorException(e);
        }
    }

    public void threadJoin(Thread thread, long millis) {
        try {
            thread.join(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ErrorException(e);
        }
    }


}
