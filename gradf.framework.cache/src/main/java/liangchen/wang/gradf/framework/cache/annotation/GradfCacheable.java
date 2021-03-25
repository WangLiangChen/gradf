package liangchen.wang.gradf.framework.cache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
@Documented
@Deprecated
public @interface GradfCacheable {
    String cacheName() default "";

    String key() default "";

    long duration() default 3600;

    String durationRange() default "";

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
