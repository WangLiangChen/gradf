package liangchen.wang.gradf.framework.cache.test;

import liangchen.wang.gradf.framework.cache.test.entity.CacheTest;
import liangchen.wang.gradf.framework.cache.test.service.ICacheTestService;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
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
//@EnableRedis
public class SpringCacheTest {
    @Inject
    private ICacheTestService service;

    @Test
    public void testCacheableMethod() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                CacheTest one = service.one();
                System.out.println("+++++++++one:" + JsonUtil.INSTANCE.toJsonString(one));
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
    }
}
