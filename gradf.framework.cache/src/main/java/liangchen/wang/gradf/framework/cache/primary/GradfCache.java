package liangchen.wang.gradf.framework.cache.primary;

import org.springframework.cache.Cache;

import java.util.Set;

/**
 * @author LiangChen.Wang
 */
public interface GradfCache extends Cache {

    Set<Object> keys();

    boolean containsKey(Object key);
}
