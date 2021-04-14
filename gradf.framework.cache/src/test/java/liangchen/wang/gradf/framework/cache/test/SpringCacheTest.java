package liangchen.wang.gradf.framework.cache.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author LiangChen.Wang 2020/9/16
 */
@SpringBootTest
//@EnableCaching
public class SpringCacheTest {
    @Inject
    private Map<String, CacheManager> cacheManagerMap;

    @Test
    public void testCacheManager() {
        System.out.println();
    }
}
