package liangchen.wang.gradf.framework.cache.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;

import javax.inject.Inject;
import java.util.concurrent.CountDownLatch;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@SpringBootTest
@EnableCaching
public class SpringCacheTest {
    @Inject
    private CacheClass cacheClass;

    @Test
    public void testCacheClass() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            new Thread(() -> {
                String s = cacheClass.cacheMethod();
                System.out.println(finalI +"---------" + s);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
    }
}
