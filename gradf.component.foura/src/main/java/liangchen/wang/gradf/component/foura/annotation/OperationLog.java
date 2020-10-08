package liangchen.wang.gradf.component.foura.annotation;

import liangchen.wang.gradf.framework.data.enumeration.OperationEnum;

import java.lang.annotation.*;

/**
 * @author LiangChen.Wang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Inherited
@Documented
public @interface OperationLog {
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

    String operationFlag() default "";

    String operationName() default "";

    OperationEnum value() default OperationEnum.NONE;
}
