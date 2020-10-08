package liangchen.wang.gradf.component.commons.annotation;

import java.lang.annotation.*;

/**
 * @author LiangChen.Wang
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.TYPE,ElementType.METHOD})
@Inherited  
@Documented
public @interface BusinessAnnotation {
	/**
	 * 业务Id
	 * @return
	 */
	long businessId() default -1;
	/**
	 * 业务标识(字母数字)
	 * @return
	 */
	String businessType();

	/**
	 * 业务名称(中文名称)
	 * @return
	 */
	String businessName();
}
