package liangchen.wang.gradf.component.foura.shiro.filter;

import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author LiangChen.Wang
 * 按角色判断是否可通过
 * defaultFilterChainManager.addFilter("roles", new RolesAuthorizationFilter());
 */
public class RolesAuthorizationFilter extends GradfFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean accessAllowed = super.isAccessAllowed(request, response, mappedValue);
        if (!accessAllowed) {
            return false;
        }
        Subject subject = this.getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;
        if (rolesArray == null || rolesArray.length == 0) {
            return false;
        }
        for (String role : rolesArray) {
            if (subject.hasRole(role)) {
                return true;
            }
        }
        // 跳到onAccessDenied处理
        return false;
    }

}
