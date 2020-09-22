package liangchen.wang.gradf.framework.cache.primary;

import org.springframework.cache.Cache;

import java.util.Set;

/**
 * @author LiangChen.Wang
 */
public interface GradfCache extends Cache {
    void putKey(String key, String originalKey);

    void evictKey(String key);

    Set<String> keys();

    boolean containsKey(String key);
}
