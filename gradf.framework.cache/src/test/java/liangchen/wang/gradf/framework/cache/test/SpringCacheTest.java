package liangchen.wang.gradf.framework.cache.test;

import liangchen.wang.gradf.framework.cache.test.entity.CacheTest;
import liangchen.wang.gradf.framework.cache.test.service.ICacheTestService;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;

import javax.inject.Inject;
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
    public void testCache() {
        CacheTest one = service.one();
        System.out.println("++++++++++one:" + JsonUtil.INSTANCE.toJsonString(one));
        ConcurrentUtil.INSTANCE.threadSleep(2, TimeUnit.SECONDS);
        one = service.one();
        System.out.println("++++++++++one:" + JsonUtil.INSTANCE.toJsonString(one));
        ConcurrentUtil.INSTANCE.threadSleep(2, TimeUnit.SECONDS);
        one = service.one();
        System.out.println("++++++++++one:" + JsonUtil.INSTANCE.toJsonString(one));
        ConcurrentUtil.INSTANCE.threadSleep(2, TimeUnit.SECONDS);
        one = service.one();
        System.out.println("++++++++++one:" + JsonUtil.INSTANCE.toJsonString(one));
        ConcurrentUtil.INSTANCE.threadSleep(2, TimeUnit.SECONDS);
        one = service.one();
        System.out.println("++++++++++one:" + JsonUtil.INSTANCE.toJsonString(one));
    }
}
