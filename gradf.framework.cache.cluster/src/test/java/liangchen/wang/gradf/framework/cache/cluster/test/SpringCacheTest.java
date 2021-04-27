package liangchen.wang.gradf.framework.cache.cluster.test;

import liangchen.wang.gradf.framework.cache.cluster.annotation.EnableRedis;
import liangchen.wang.gradf.framework.cache.cluster.test.entity.CacheTest;
import liangchen.wang.gradf.framework.cache.cluster.test.service.ICacheTestService;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@SpringBootTest
@EnableCaching
@EnableRedis
public class SpringCacheTest {
    @Inject
    private ICacheTestService service;

    @Test
    public void testCache() throws InterruptedException {
        int number = 10;
        CountDownLatch countDownLatch = new CountDownLatch(number);
        for (int i = 0; i < number; i++) {
            new Thread(() -> {
                CacheTest one = service.one();
                System.out.println("++++++++++++++++++++++++++++++" + JsonUtil.INSTANCE.toJsonString(one));
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
    }
}