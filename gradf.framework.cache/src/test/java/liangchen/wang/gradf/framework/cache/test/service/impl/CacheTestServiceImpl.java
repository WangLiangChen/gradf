package liangchen.wang.gradf.framework.cache.test.service.impl;

import liangchen.wang.gradf.framework.cache.annotation.Cacheable;
import liangchen.wang.gradf.framework.cache.test.service.AbstractCacheTestService;
import org.springframework.stereotype.Component;

@Component
@Cacheable(cacheNames = "CacheTest")
public class CacheTestServiceImpl extends AbstractCacheTestService {
    @Override
    public String update(String text) {
        String ret = "hello!update:" + text;
        System.out.println("+++++++++" + ret);
        return ret;
    }
}
