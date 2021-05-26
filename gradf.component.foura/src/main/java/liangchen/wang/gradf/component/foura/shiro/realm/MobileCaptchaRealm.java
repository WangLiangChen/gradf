package liangchen.wang.gradf.component.foura.shiro.realm;

import liangchen.wang.gradf.component.foura.manager.domain.result.AccountPasswordResultDomain;
import liangchen.wang.gradf.component.foura.shiro.token.MobileCaptchaToken;
import liangchen.wang.gradf.component.foura.utils.FouraUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LiangChen.Wang
 * 手机号+验证码
 */
public class MobileCaptchaRealm extends StatelessRealm {
    private static final Logger logger = LoggerFactory.getLogger(MobileCaptchaRealm.class);

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token instanceof MobileCaptchaToken) {
            return true;
        }
        return false;
    }

    /**
     * 手机号验证码登录方式，改写获取认证信息
     *
     * @param token
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        Object principal = token.getPrincipal();
        logger.debug("principal is : {}", principal);
        String mobile = (String) principal;
        AccountPasswordResultDomain accountPassword = FouraUtil.INSTANCE.authenticationInfoByKey(mobile);
        Long account_id = accountPassword.getAccount_id();
        String login_name = accountPassword.getLogin_name();

        SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
        String realmName = getName();
        principalCollection.add(account_id, realmName);
        principalCollection.add(login_name, realmName);
        return new SimpleAuthenticationInfo(principalCollection, token.getCredentials());
    }
}
