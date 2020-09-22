package liangchen.wang.gradf.framework.cache.primary;


import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.validator.Assert;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 */
public class CacheNameResolver {
    private long ttl = -1;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private String name;

    public CacheNameResolver(String cacheName) {
        Assert.INSTANCE.notBlank(cacheName, "缓存名称不能为空");
        String[] split = cacheName.split("@");
        //真正的缓存名称
        name = split[0];
        if (split.length == 1) {
            return;
        }
        if (split.length > 2) {
            throw new InfoException("缓存名称只能包含一个@,@后的值为缓存有效时长");
        }
        String _ttl = split[1];
        try {
            ttl = Long.parseLong(_ttl);
        } catch (Exception e) {
            throw new InfoException("有效时长必须为整数");
        }
    }

    public long getTtl() {
        return ttl;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public String getName() {
        return name;
    }
}
