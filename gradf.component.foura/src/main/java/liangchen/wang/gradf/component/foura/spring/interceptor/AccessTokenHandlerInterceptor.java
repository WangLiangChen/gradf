package liangchen.wang.gradf.component.foura.spring.interceptor;

import liangchen.wang.gradf.component.foura.utils.FouraUtil;
import liangchen.wang.gradf.framework.commons.utils.ContextUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 将当前登录账户Id和名称放入ThreadLocal
 *
 * @author LiangChen.Wang
 */
public class AccessTokenHandlerInterceptor implements AsyncHandlerInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(AccessTokenHandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Subject subject = SecurityUtils.getSubject();
        if (null == subject) {
            return true;
        }
        try {
            Long account_id = (Long) subject.getPrincipal();
            if (null == account_id) {
                return true;
            }
            FouraUtil.INSTANCE.putOperator(account_id);
            PrincipalCollection principals = subject.getPrincipals();
            FouraUtil.INSTANCE.setOperatorName(String.valueOf(principals.asList().get(1)));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) {
        ContextUtil.INSTANCE.clear();
        ContextUtil.INSTANCE.remove();
    }

}
