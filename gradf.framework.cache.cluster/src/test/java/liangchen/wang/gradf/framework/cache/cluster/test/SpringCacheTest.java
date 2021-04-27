package liangchen.wang.gradf.framework.cache.cluster.test;

import liangchen.wang.gradf.framework.cache.cluster.annotation.EnableRedis;
import liangchen.wang.gradf.framework.cache.cluster.test.entity.CacheTest;
import liangchen.wang.gradf.framework.cache.cluster.test.service.ICacheTestService;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
    public void testRedisCache() throws InterruptedException {
        CacheTest one = service.one();
        System.out.println("++++++++++++++++++++++++++++++" + JsonUtil.INSTANCE.toJsonString(one));
        ConcurrentUtil.INSTANCE.threadSleep(2,TimeUnit.SECONDS);
        one = service.one();
        System.out.println("++++++++++++++++++++++++++++++" + JsonUtil.INSTANCE.toJsonString(one));
        ConcurrentUtil.INSTANCE.threadSleep(2,TimeUnit.SECONDS);
        one = service.one();
        System.out.println("++++++++++++++++++++++++++++++" + JsonUtil.INSTANCE.toJsonString(one));
        ConcurrentUtil.INSTANCE.threadSleep(2,TimeUnit.SECONDS);
        one = service.one();
        System.out.println("++++++++++++++++++++++++++++++" + JsonUtil.INSTANCE.toJsonString(one));
        ConcurrentUtil.INSTANCE.threadSleep(2,TimeUnit.SECONDS);
        one = service.one();
        System.out.println("++++++++++++++++++++++++++++++" + JsonUtil.INSTANCE.toJsonString(one));
        ConcurrentUtil.INSTANCE.threadSleep(2,TimeUnit.SECONDS);one = service.one();
        System.out.println("++++++++++++++++++++++++++++++" + JsonUtil.INSTANCE.toJsonString(one));
        ConcurrentUtil.INSTANCE.threadSleep(2,TimeUnit.SECONDS);
        one = service.one();
        System.out.println("++++++++++++++++++++++++++++++" + JsonUtil.INSTANCE.toJsonString(one));
        ConcurrentUtil.INSTANCE.threadSleep(2,TimeUnit.SECONDS);
        one = service.one();
        System.out.println("++++++++++++++++++++++++++++++" + JsonUtil.INSTANCE.toJsonString(one));
    }
}