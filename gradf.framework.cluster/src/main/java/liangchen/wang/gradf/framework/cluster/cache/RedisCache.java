package liangchen.wang.gradf.framework.cluster.cache;

import liangchen.wang.gradf.framework.cache.primary.GradfCache;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

/**
 * 扩展补充spring的RedisCache
 *
 * @author LiangChen.Wang
 */
public class RedisCache extends org.springframework.data.redis.cache.RedisCache implements GradfCache {
    private final static Logger logger = LoggerFactory.getLogger(RedisCache.class);
    private final BoundHashOperations<String, String, Object> boundHashOperations;

    public RedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig, StringRedisTemplate stringRedisTemplate) {
        super(name, cacheWriter, cacheConfig);
        String keys = this.createCacheKey("keys");
        this.boundHashOperations = stringRedisTemplate.boundHashOps(keys);
    }

    @Override
    public void putKey(String key, String originalKey) {
        Assert.INSTANCE.notBlank(key, "key不能为空");
        Assert.INSTANCE.notBlank(originalKey, "originalKey不能为空");
        logger.debug("RedisCache,putKey,HashKey:{},key:{},originalKey:{}", boundHashOperations.getKey(), key, originalKey);
        boundHashOperations.put(key, originalKey);
    }

    @Override
    public void evictKey(String key) {
        Assert.INSTANCE.notBlank(key, "key不能为空");
        logger.debug("RedisCache,evictKey,HashKey:{},key:{}", boundHashOperations.getKey(), key);
        boundHashOperations.delete(key);
    }

    @Override
    public Set<String> keys() {
        return boundHashOperations.keys();
    }

    @Override
    public boolean containsKey(String key) {
        Assert.INSTANCE.notBlank(key, "key不能为空");
        return boundHashOperations.hasKey(key);
    }
}
