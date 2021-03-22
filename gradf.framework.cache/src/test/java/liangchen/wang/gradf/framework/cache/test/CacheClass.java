package liangchen.wang.gradf.framework.cache.test;

import liangchen.wang.gradf.framework.cache.annotation.CachePut;
import liangchen.wang.gradf.framework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;


/**
 * @author LiangChen.Wang 2020/9/21
 */
@Component
public class CacheClass {
    @CachePut(value = "testCache", ttl = 1000)
    public String cacheMethod() {
        System.out.println("=======================>test----null");
        return "I am LiangChen.Wang";
    }
}
