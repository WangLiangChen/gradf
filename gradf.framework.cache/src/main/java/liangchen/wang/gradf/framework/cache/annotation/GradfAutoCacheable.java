package liangchen.wang.gradf.framework.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GradfAutoCacheable {
    String[] clearMethods() default {};

    String[] excludeMethods() default {};

    long duration() default 3600;

    String durationRange() default "";

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
