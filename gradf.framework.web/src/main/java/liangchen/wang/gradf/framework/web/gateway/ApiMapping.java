package liangchen.wang.gradf.framework.web.gateway;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author LiangChen.Wang
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ApiMapping {
    String apiName();

    RequestMethod requestMethod();
}
