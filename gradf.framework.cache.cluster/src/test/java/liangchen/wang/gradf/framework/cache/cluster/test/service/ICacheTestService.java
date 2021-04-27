package liangchen.wang.gradf.framework.cache.cluster.test.service;


import liangchen.wang.gradf.framework.cache.cluster.test.entity.CacheTest;

import java.util.List;

public interface ICacheTestService {
    CacheTest one();

    List<CacheTest> list(CacheTest cacheTest);

    Long insert(CacheTest cacheTest);

    int update(CacheTest cacheTest);

    void delete(Long cacheTestId);

    void deleteAll();
}
