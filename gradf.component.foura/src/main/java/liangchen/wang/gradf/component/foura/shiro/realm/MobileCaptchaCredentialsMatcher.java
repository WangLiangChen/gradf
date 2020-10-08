package liangchen.wang.gradf.component.foura.shiro.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LiangChen.Wang
 */
public class MobileCaptchaCredentialsMatcher implements CredentialsMatcher {
    private static final Logger logger = LoggerFactory.getLogger(MobileCaptchaCredentialsMatcher.class);

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        return true;
    }
}
