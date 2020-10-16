package liangchen.wang.gradf.framework.springboot.configuration;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author LiangChen.Wang
 */
@EnableAsync
@EnableScheduling
public class ThreadPoolAutoConfiguration implements AsyncConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolAutoConfiguration.class);

    @Primary
    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int processors = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(processors + 2);
        executor.setMaxPoolSize(processors * 16);
        executor.setQueueCapacity(processors * 16);
        executor.setThreadNamePrefix("async_");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        //jvm退出时关闭task，解决使用tomcat的shutdown后进程依然存在的问题
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                executor.shutdown();
            }
        });
        /*使用TransmittableThreadLocal 必须用Ttl**包装一下
          TtlRunnable和TtlCallable来包装Runnable和Callable。
          getTtlExecutor包装Executor
          getTtlExecutorService包装ExecutorService
          getTtlScheduledExecutorService包装ScheduledExecutorService
        */
        return TtlExecutors.getTtlExecutor(executor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> logger.error("异步线程执行异常", ex);
    }
}
