package liangchen.wang.gradf.framework.commons.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public enum ThreadPoolUtil {
    //
    INSTANCE;
    private final ExecutorService executorService;

    ThreadPoolUtil() {
        // 核心线程数0,最大线程数Integer.MAX_VALUE,空闲线程超时时间60 SECONDS , 线程等待队列SynchronousQueue(容量为0的队列)
        executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                new SynchronousQueue<>(), getThreadFactory("unbounded-executor-", false));
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public ThreadFactory getThreadFactory(String threadName, boolean daemon) {
        ThreadFactory threadFactory = new ThreadFactory() {
            private AtomicInteger counter = new AtomicInteger();

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setDaemon(daemon);
                thread.setName(String.format("%s%d", threadName, counter.incrementAndGet()));
                return thread;
            }
        };
        return threadFactory;
    }
}
