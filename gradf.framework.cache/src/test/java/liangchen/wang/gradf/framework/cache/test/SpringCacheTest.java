package liangchen.wang.gradf.framework.cache.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;

import javax.inject.Inject;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@SpringBootTest
@EnableCaching
public class SpringCacheTest {
    @Inject
    private CacheClass cacheClass;

    @Test
    public void testCacheClass() {
        cacheClass.cacheMethod();
    }
}
