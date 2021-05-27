package liangchen.wang.gradf.framework.data.test;

import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import liangchen.wang.gradf.framework.data.annotation.EnableJdbc;
import liangchen.wang.gradf.framework.data.annotation.EnableJdbcShedLock;
import net.javacrumbs.shedlock.core.DefaultLockingTaskExecutor;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@SpringBootTest
@EnableJdbc
@EnableJdbcShedLock
public class DataTest {
    @Inject
    private LockProvider lockProvider;

    @Test
    public void testDataSourceProperties() {
        System.out.println();
    }

    @Test
    public void testShedLock() {
        LockingTaskExecutor executor = new DefaultLockingTaskExecutor(lockProvider);
        Instant now = Instant.now();
        executor.executeWithLock((Runnable) () -> {
            System.out.println("====>" + Thread.currentThread().getName());
            ConcurrentUtil.INSTANCE.threadSleep(10, TimeUnit.SECONDS);
        }, new LockConfiguration(now, "wanglc", Duration.ofSeconds(60), Duration.ofSeconds(10)));
        ConcurrentUtil.INSTANCE.threadSleep(200, TimeUnit.SECONDS);
    }
}