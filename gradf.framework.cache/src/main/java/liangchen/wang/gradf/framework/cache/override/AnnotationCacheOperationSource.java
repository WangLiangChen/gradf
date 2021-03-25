package liangchen.wang.gradf.framework.cache.override;

import org.springframework.cache.annotation.CacheAnnotationParser;
import org.springframework.cache.interceptor.CacheOperation;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author LiangChen.Wang 2021/3/25
 */
public class AnnotationCacheOperationSource extends org.springframework.cache.annotation.AnnotationCacheOperationSource {
    public AnnotationCacheOperationSource(CacheAnnotationParser annotationParser) {
        super(annotationParser);
    }

    @Override
    public Collection<CacheOperation> getCacheOperations(Method method, Class<?> targetClass) {
        Collection<CacheOperation> cacheOperations = super.getCacheOperations(method, targetClass);
        //TODO 尝试在这里解决父类方法不覆写不能切入的问题
        return cacheOperations;
    }
}
