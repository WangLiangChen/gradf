package liangchen.wang.gradf.component.foura.shiro.authc;

import liangchen.wang.gradf.component.foura.shiro.realm.JwtRealm;
import liangchen.wang.gradf.component.foura.shiro.token.JwtToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;

/**
 * 如果是JwtToken则使用单Realm认证器
 *
 * @author LiangChen.Wang
 */
public class ModularRealmAuthenticator extends org.apache.shiro.authc.pam.ModularRealmAuthenticator {
    private static final Logger logger = LoggerFactory.getLogger(ModularRealmAuthenticator.class);

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.debug("token is:{}", authenticationToken);
        if (!(authenticationToken instanceof JwtToken)) {
            return super.doAuthenticate(authenticationToken);
        }
        assertRealmsConfigured();
        //如果是JwtToken则使用doSingleRealmAuthentication
        Collection<Realm> realms = getRealms();
        Iterator<Realm> it = realms.iterator();
        Realm realm = null;
        //根据token类型取出一个对应的realm
        while (it.hasNext()) {
            realm = it.next();
            if (realm instanceof JwtRealm) {
                break;
            }
        }
        logger.debug("realm is:{}", realm);
        return doSingleRealmAuthentication(realm, authenticationToken);
    }

}
