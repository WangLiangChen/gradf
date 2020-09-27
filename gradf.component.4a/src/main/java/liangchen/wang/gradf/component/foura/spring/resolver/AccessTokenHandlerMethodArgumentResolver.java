package liangchen.wang.gradf.component.foura.spring.resolver;

import liangchen.wang.gradf.component.foura.annotation.AccountId;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author LiangChen.Wang
 */
//需要在Configuration中注入，addArgumentResolvers
public class AccessTokenHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest nativeWebRequest, WebDataBinderFactory factory) {
        Subject subject = SecurityUtils.getSubject();
        if (null == subject) {
            return null;
        }
        Long account_id = (Long) subject.getPrincipal();
        return account_id;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AccountId.class);
    }

}
