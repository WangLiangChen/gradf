package liangchen.wang.gradf.component.foura.annotation;

import java.lang.annotation.*;

/**
 * @author LiangChen.Wang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Documented
public @interface EnableOperationLog {
    /**
     * 业务标识(字母数字)
     *
     * @return
     */
    String businessType() default "";

    /**
     * 业务名称(中文名称)
     *
     * @return
     */
    String businessName() default "";
}
