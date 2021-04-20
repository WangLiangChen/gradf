package liangchen.wang.gradf.framework.cache.test.service.impl;

import liangchen.wang.gradf.framework.cache.annotation.Cacheable;
import liangchen.wang.gradf.framework.cache.test.entity.CacheTest;
import liangchen.wang.gradf.framework.cache.test.service.AbstractCacheTestService;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import org.springframework.stereotype.Component;

@Component
public class CacheTestServiceImpl extends AbstractCacheTestService {
    @Override
    @Cacheable(cacheNames = "CacheTest", sync = true,ttl=5000)
    public CacheTest one() {
        CacheTest cacheTest = new CacheTest();
        cacheTest.setCacheTestId(110L);
        cacheTest.setCacheTestText("110");
        System.out.println("-----------------------one:" + JsonUtil.INSTANCE.toJsonString(cacheTest));
        return cacheTest;
    }

    @Override
    public Long insert(CacheTest cacheTest) {
        System.out.println("--------------inset:" + JsonUtil.INSTANCE.toJsonString(cacheTest));
        return 0L;
    }

    @Override
    public int update(CacheTest cacheTest) {
        System.out.println("--------------inset:" + JsonUtil.INSTANCE.toJsonString(cacheTest));
        return 2;
    }

    @Override
    public void delete(Long cacheTestId) {
        System.out.println("--------------delete:" + cacheTestId);
    }

    @Override
    public void deleteAll() {
        System.out.println("--------------deleteAll");
    }
}
