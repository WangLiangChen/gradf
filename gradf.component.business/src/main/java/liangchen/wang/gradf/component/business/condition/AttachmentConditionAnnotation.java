package liangchen.wang.gradf.component.business.condition;


import liangchen.wang.gradf.component.business.enumeration.AttachmentStorageProvider;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author LiangChen.Wang 2019/9/17 20:40
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(AttachmentCondition.class)
public @interface AttachmentConditionAnnotation {
    AttachmentStorageProvider havingValue();
}
