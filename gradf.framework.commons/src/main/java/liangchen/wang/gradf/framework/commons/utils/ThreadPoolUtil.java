package liangchen.wang.gradf.framework.commons.utils;

import okhttp3.internal.Util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public enum ThreadPoolUtil {
    //
    INSTANCE;
    private final ExecutorService executorService;

    ThreadPoolUtil() {
        // 核心线程数0,最大线程数Integer.MAX_VALUE,空闲线程超时时间60 SECONDS , 线程等待队列SynchronousQueue(容量为0的队列)
        executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                new SynchronousQueue<>(), Util.threadFactory("unbounded-executor-", false));
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
