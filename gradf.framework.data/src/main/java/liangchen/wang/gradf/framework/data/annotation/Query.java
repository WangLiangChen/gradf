package liangchen.wang.gradf.framework.data.annotation;


import liangchen.wang.gradf.framework.data.enumeration.AndOr;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import java.lang.annotation.*;

/**
 * @author .LiangChen.Wang
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Query {
    AndOr andOr() default AndOr.AND;

    Operator operator();

    String column() default "";

    String group() default "";
}
