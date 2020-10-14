package liangchen.wang.gradf.component.foura.shiro.permission;

import liangchen.wang.gradf.framework.commons.enumeration.Symbol;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * @author LiangChen.Wang
 */
public class BitAndWildPermissionResolver implements PermissionResolver {
    @Override
    public Permission resolvePermission(String permissionString) {
        if (permissionString.contains(Symbol.PLUS.getSymbol())) {
            return new BitPermission(permissionString);
        }
        return new WildcardPermission(permissionString);
    }
}
