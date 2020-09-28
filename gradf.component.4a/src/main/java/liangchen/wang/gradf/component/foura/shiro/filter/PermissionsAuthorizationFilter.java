package liangchen.wang.gradf.component.foura.shiro.filter;

import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author LiangChen.Wang
 * 按照权限值判断是否通过
 * defaultFilterChainManager.addFilter("perms", new PermissionsAuthorizationFilter());
 */
public class PermissionsAuthorizationFilter extends GradfFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean accessAllowed = super.isAccessAllowed(request, response, mappedValue);
        if (!accessAllowed) {
            return false;
        }

        Subject subject = getSubject(request, response);
        String[] perms = (String[]) mappedValue;

        boolean isPermitted = true;
        if (perms != null && perms.length > 0) {
            if (perms.length == 1) {
                if (!subject.isPermitted(perms[0])) {
                    isPermitted = false;
                }
            } else {
                if (!subject.isPermittedAll(perms)) {
                    isPermitted = false;
                }
            }
        }
        // 跳到onAccessDenied处理
        return isPermitted;
    }

}
