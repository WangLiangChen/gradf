package liangchen.wang.gradf.framework.springboot.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OverrideBeanName {
    String value();
}
