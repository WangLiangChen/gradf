package liangchen.wang.gradf.framework.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import liangchen.wang.gradf.framework.cache.primary.GradfCache;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author LiangChen.Wang
 */
public class GradfCaffeineCache extends CaffeineCache implements GradfCache {
    private final static Logger logger = LoggerFactory.getLogger(GradfCaffeineCache.class);
    private final ConcurrentMap<String, String> keys;

    public GradfCaffeineCache(String name, Cache<Object, Object> cache) {
        super(name, cache, true);
        keys = new ConcurrentHashMap<>();
    }

    @Override
    public void putKey(String key, String originalKey) {
        Assert.INSTANCE.notBlank(key, "key不能为空");
        Assert.INSTANCE.notBlank(originalKey, "originalKey不能为空");
        logger.debug("CaffeineCache,putKey,Key:{},originalKey:{}", key, originalKey);
        keys.put(key, originalKey);
    }

    @Override
    public void evictKey(String key) {
        Assert.INSTANCE.notBlank(key, "key不能为空");
        logger.debug("CaffeineCache,evictKey,Key:{}", key);
        keys.remove(key);
    }

    @Override
    public Set<String> keys() {
        return keys.keySet();
    }

    @Override
    public boolean containsKey(String key) {
        Assert.INSTANCE.notBlank(key, "key不能为空");
        return keys.containsKey(key);
    }

}
