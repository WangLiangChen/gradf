package liangchen.wang.gradf.component.foura.shiro.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;

import java.util.Collection;
import java.util.Collections;

/**
 * @author LiangChen.Wang
 */
public class DefaultRolePermissionResolver implements RolePermissionResolver {
    @Override
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        return Collections.emptyList();
    }
}
