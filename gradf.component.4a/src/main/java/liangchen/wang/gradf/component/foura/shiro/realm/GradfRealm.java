package liangchen.wang.gradf.component.foura.shiro.realm;

import liangchen.wang.gradf.component.foura.utils.FouraUtil;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

/**
 * @author LiangChen.Wang
 */
public abstract class GradfRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(GradfRealm.class);

    /**
     * 改写获取授权信息，根据认证信息里的account_id获取对应的role
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object primaryPrincipal = principals.getPrimaryPrincipal();
        logger.debug("primaryPrincipal is:{}", primaryPrincipal);
        Subject subject = SecurityUtils.getSubject();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 因为非正常退出，即没有显式调用 SecurityUtils.getSubject().logout(),(可能是关闭浏览器，或超时)，但此时缓存依旧存在(principals)，要判断一下。
        if (!subject.isAuthenticated()) {
            doClearCache(principals);
            subject.logout();
            return authorizationInfo;
        }
        // principalCollection
        Collection<?> collection = principals.fromRealm(getName());
        if (collection.isEmpty()) {
            return authorizationInfo;
        }

        Long account_id = (Long) primaryPrincipal;
        Set<String> roleIds = FouraUtil.INSTANCE.roleIdsByAccountId(account_id);
        logger.debug("account_id is:{},roleIdsByAccountId is:{}", account_id, roleIds.toString());
        if (CollectionUtil.INSTANCE.isNotEmpty(roleIds)) {
            authorizationInfo.setRoles(roleIds);
        }
        Set<String> permissions = FouraUtil.INSTANCE.permissionsByAccountId(account_id);
        if (CollectionUtil.INSTANCE.isNotEmpty(permissions)) {
            authorizationInfo.setStringPermissions(permissions);
        }
        /*用户Premissions来源构成,以下三个的合集
         * 1、authorizationInfo.setObjectPermissions
         * 2、authorizationInfo.setStringPermissions
         * 3、RolePermissionResolver.resolvePermissionsInRole
         * */
        return authorizationInfo;
    }
}
