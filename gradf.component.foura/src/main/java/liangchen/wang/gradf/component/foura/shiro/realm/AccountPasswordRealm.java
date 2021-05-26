package liangchen.wang.gradf.component.foura.shiro.realm;

import liangchen.wang.gradf.component.foura.manager.domain.result.AccountPasswordResultDomain;
import liangchen.wang.gradf.component.foura.utils.FouraUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LiangChen.Wang
 */
public class AccountPasswordRealm extends StatelessRealm {
    private static final Logger logger = LoggerFactory.getLogger(AccountPasswordRealm.class);

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof UsernamePasswordToken) {
            return true;
        }
        return false;
    }

    /**
     * 密码登录方式，改写获取认证信息
     *
     * @param token
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        Object principal = token.getPrincipal();
        logger.debug("principal is : {}", principal);
        String username = (String) principal;
        AccountPasswordResultDomain accountPassword = FouraUtil.INSTANCE.authenticationInfoByKey(username);
        Long account_id = accountPassword.getAccount_id();
        String login_name = accountPassword.getLogin_name();
        String hashedCredentials = accountPassword.getLogin_password();
        String salt = accountPassword.getPassword_salt();

        //这里的salt要跟PasswordUtil中计算密码使用的salt一样
        salt = account_id + salt;
        ByteSource credentialsSalt = ByteSource.Util.bytes(salt);
        SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
        String realmName = getName();
        principalCollection.add(account_id, realmName);
        principalCollection.add(login_name, realmName);
        return new SimpleAuthenticationInfo(principalCollection, hashedCredentials, credentialsSalt);
    }
}
