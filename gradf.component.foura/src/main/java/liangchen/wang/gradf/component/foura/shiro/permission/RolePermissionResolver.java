package liangchen.wang.gradf.component.foura.shiro.permission;

import org.apache.shiro.authz.Permission;

import java.util.Collection;
import java.util.Collections;

/**
 * @author LiangChen.Wang
 */
public class RolePermissionResolver implements  org.apache.shiro.authz.permission.RolePermissionResolver {
    @Override
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        return Collections.emptyList();
    }
}