package liangchen.wang.gradf.framework.cache.test.service;

public abstract class AbstractCacheTestService implements ICacheTestService {
    @Override
    public String insert(String text) {
        String ret = "hello!abstract insert:" + text;
        System.out.println("+++++++++" + ret);
        return ret;
    }
}
