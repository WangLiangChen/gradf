package liangchen.wang.gradf.framework.cache.test.service;

import liangchen.wang.gradf.framework.cache.annotation.Cacheable;
import liangchen.wang.gradf.framework.cache.test.entity.CacheTest;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCacheTestService implements ICacheTestService {
    @Override
    public List<CacheTest> list(CacheTest cacheTest) {
        System.out.println("-----------------" + JsonUtil.INSTANCE.toJsonString(cacheTest));
        List<CacheTest> list = new ArrayList<>();
        CacheTest e = new CacheTest();
        e.setCacheTestId(1l);
        e.setCacheTestText("1");
        list.add(e);
        e = new CacheTest();
        e.setCacheTestId(2l);
        e.setCacheTestText("2");
        list.add(e);
        return list;
    }

}
