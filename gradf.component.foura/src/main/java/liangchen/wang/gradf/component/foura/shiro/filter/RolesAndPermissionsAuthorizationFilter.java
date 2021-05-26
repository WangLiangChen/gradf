package liangchen.wang.gradf.component.foura.shiro.filter;

import liangchen.wang.gradf.framework.commons.enumeration.Symbol;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import org.apache.shiro.subject.Subject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author LiangChen.Wang
 * 按角色/权限/位权限判断是否可通过
 * defaultFilterChainManager.addFilter("roles", new RolesAuthorizationFilter());
 */
public class RolesAndPermissionsAuthorizationFilter extends StatelessFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean accessAllowed = super.isAccessAllowed(request, response, mappedValue);
        if (!accessAllowed) {
            return false;
        }
        Subject subject = this.getSubject(request, response);
        String[] mappedArray = (String[]) mappedValue;
        if (CollectionUtil.INSTANCE.isEmpty(mappedArray)) {
            return false;
        }
        for (String value : mappedArray) {
            if (value.contains(Symbol.COLON.getSymbol()) || value.contains(Symbol.PLUS.getSymbol())) {
                // 其实是委托执行realm的isPermitted
                if (subject.isPermitted(value)) {
                    return true;
                }
            } else {
                // 其实是委托执行realm的hasRole
                if (subject.hasRole(value)) {
                    return true;
                }
            }
        }
        // 跳到onAccessDenied处理
        return false;
    }

}
