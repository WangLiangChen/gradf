package liangchen.wang.gradf.framework.cache.test;

import liangchen.wang.gradf.framework.cache.annotation.EnableRedis;
import liangchen.wang.gradf.framework.cache.redis.CacheMessage;
import liangchen.wang.gradf.framework.cache.test.service.ICacheTestService;
import liangchen.wang.gradf.framework.commons.utils.ConcurrentUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.inject.Inject;
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
    @Inject
    private StringRedisTemplate stringRedisTemplate;

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
    public void testRedisStream() {
        ObjectRecord<String, CacheMessage> record = StreamRecords.newRecord().ofObject(CacheMessage.newInstance("test", CacheMessage.CacheAction.none)).withStreamKey("channel:stream:expire");
        StreamOperations<String, Object, Object> streamOperations = stringRedisTemplate.opsForStream();
        RecordId initialRecord = streamOperations.add(record);
        ConcurrentUtil.INSTANCE.threadSleep(10, TimeUnit.SECONDS);
    }
}
