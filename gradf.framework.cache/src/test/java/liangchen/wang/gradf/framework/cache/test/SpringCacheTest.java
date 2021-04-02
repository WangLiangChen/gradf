package liangchen.wang.gradf.framework.cache.test;

import liangchen.wang.gradf.framework.cache.annotation.EnableRedis;
import liangchen.wang.gradf.framework.cache.redis.CacheMessage;
import liangchen.wang.gradf.framework.cache.test.service.ICacheTestService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;

import javax.inject.Inject;

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
    public void testCacheable() throws InterruptedException {
        String ret = service.insert("a");
        System.out.println("---------" + ret);
        ret = service.insert("a");
        System.out.println("---------" + ret);
        ret = service.update("a");
        System.out.println("---------" + ret);
        ret = service.update("a");
        System.out.println("---------" + ret);
    }
    @Test
    public void testRedisStream(){
        RedisTemplate<String, ObjectRecord<String, CacheMessage>> redisTemplate = new RedisTemplate<>();
    }
}
