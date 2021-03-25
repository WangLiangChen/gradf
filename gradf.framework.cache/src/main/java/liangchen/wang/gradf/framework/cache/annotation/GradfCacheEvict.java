package liangchen.wang.gradf.framework.cache.annotation;

import java.lang.annotation.*;

/**
 * @author LiangChen.Wang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
@Documented
@Deprecated
public @interface GradfCacheEvict {
    String cacheName() default "";

    String key() default "";
}
