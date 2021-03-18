package liangchen.wang.gradf.framework.cache.test;

import liangchen.wang.gradf.framework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author LiangChen.Wang 2020/9/21
 */
@Cacheable
@Component
public class CacheClass {
    public String cacheMethod() {
        System.out.println("=======================>test----null");
        return null;
    }
}
