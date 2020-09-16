package liangchen.wang.gradf.framework.commons.logger.annotation;

import java.lang.annotation.*;

/**
 * @author LiangChen.Wang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@Documented
public @interface LoggerAnnotation {
    LoggerLevel value();
}
