package liangchen.wang.gradf.framework.cache.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@org.springframework.cache.annotation.Cacheable
public @interface Cacheable {
}
