package liangchen.wang.gradf.component.foura.shiro.filter;

import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author LiangChen.Wang
 * 登录状态即可通过
 * defaultFilterChainManager.addFilter("login", new LoginAuthorizationFilter());
 */
public class LoginAuthorizationFilter extends GradfFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean accessAllowed = super.isAccessAllowed(request, response, mappedValue);
        if (!accessAllowed) {
            return false;
        }
        Subject subject = getSubject(request, response);
        if (subject.isAuthenticated()) {
            return true;
        }
        // 跳到onAccessDenied处理
        return false;
    }
}
