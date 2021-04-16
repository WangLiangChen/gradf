package liangchen.wang.gradf.framework.cache.cluster.test;

import liangchen.wang.gradf.framework.cache.test.entity.CacheTest;
import liangchen.wang.gradf.framework.cache.test.service.ICacheTestService;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;

import javax.inject.Inject;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@SpringBootTest
@EnableCaching
public class SpringCacheTest {
    @Inject
    private ICacheTestService service;

    @Test
    public void testCacheManager() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(30);
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                CacheTest one = service.one();
                System.out.println(Thread.currentThread().getId() + "+++++++++++++++++++++++one:" + JsonUtil.INSTANCE.toJsonString(one));
                ConcurrentUtil.INSTANCE.threadSleep(3, TimeUnit.MILLISECONDS);
                one = service.one();
                System.out.println(Thread.currentThread().getId() + "+++++++++++++++++++++++one:" + JsonUtil.INSTANCE.toJsonString(one));
                ConcurrentUtil.INSTANCE.threadSleep(3, TimeUnit.MILLISECONDS);
                one = service.one();
                System.out.println(Thread.currentThread().getId() + "+++++++++++++++++++++++one:" + JsonUtil.INSTANCE.toJsonString(one));
                ConcurrentUtil.INSTANCE.threadSleep(3, TimeUnit.MILLISECONDS);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
    }
}
