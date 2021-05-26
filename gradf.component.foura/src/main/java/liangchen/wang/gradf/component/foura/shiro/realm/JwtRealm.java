package liangchen.wang.gradf.component.foura.shiro.realm;

import liangchen.wang.gradf.component.foura.manager.domain.result.AccountPasswordResultDomain;
import liangchen.wang.gradf.component.foura.shiro.token.JwtToken;
import liangchen.wang.gradf.component.foura.utils.FouraUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LiangChen.Wang
 */
public class JwtRealm extends StatelessRealm {
    private static final Logger logger = LoggerFactory.getLogger(JwtRealm.class);

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof JwtToken) {
            return true;
        }
        return false;
    }

    /**
     * 改写JWT验证方式获取验证信息
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        Object principal = token.getPrincipal();
        logger.debug("principal is : {}", principal);
        String tokenString = (String) principal;
        if (StringUtils.isEmpty(tokenString)) {
            throw new AuthenticationException("token不存在");
        }
        logger.debug("access_token is :{}", tokenString);
        AccountPasswordResultDomain accountPassword = FouraUtil.INSTANCE.authenticationInfoByJwt(tokenString);
        SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
        String realmName = getName();
        principalCollection.add(accountPassword.getAccount_id(), realmName);
        principalCollection.add(accountPassword.getLogin_name(), realmName);
        return new SimpleAuthenticationInfo(principalCollection, "");
    }

}
