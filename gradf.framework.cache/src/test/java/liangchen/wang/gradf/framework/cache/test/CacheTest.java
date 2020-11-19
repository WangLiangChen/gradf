package liangchen.wang.gradf.framework.cache.test;

import liangchen.wang.gradf.framework.cache.annotation.EnableGradfCaching;
import liangchen.wang.gradf.framework.cache.primary.GradfCache;
import liangchen.wang.gradf.framework.cache.primary.GradfCacheManager;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@SpringBootTest
@EnableGradfCaching
public class CacheTest {
    @Inject
    private GradfCacheManager cacheManager;
    @Inject
    private Executor executor;
    @Inject
    private CacheClass cacheClass;

    @Test
    public void testManager() {
        System.out.println(cacheManager);
    }

    @Test
    public void testCache() {
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                GradfCache c = cacheManager.getCache("wanglc", 1, TimeUnit.MINUTES);
                System.out.println(c);
            });
        }
        ConcurrentUtil.INSTANCE.threadSleep(10, TimeUnit.SECONDS);
    }

    @Test
    public void testCacheNull() {
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                String s = cacheClass.cacheMethod();
                System.out.println("---------->" + s);
            });
        }
        ConcurrentUtil.INSTANCE.threadSleep(10, TimeUnit.SECONDS);
    }
}
