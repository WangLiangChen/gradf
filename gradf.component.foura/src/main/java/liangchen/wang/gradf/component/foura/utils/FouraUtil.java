package liangchen.wang.gradf.component.foura.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import liangchen.wang.gradf.component.foura.manager.IAuthenticationManager;
import liangchen.wang.gradf.component.foura.manager.IAuthorizationManager;
import liangchen.wang.gradf.component.foura.manager.domain.result.AccountPasswordResultDomain;
import liangchen.wang.gradf.framework.commons.utils.ContextUtil;
import liangchen.wang.gradf.framework.springboot.context.BeanLoader;
import liangchen.wang.gradf.framework.web.jwts.AccessTokenUtil;

import java.util.Set;

/**
 * @author LiangChen.Wang
 */
public enum FouraUtil {
    /**
     * INSTANCE;
     */
    INSTANCE;
    private IAuthorizationManager authorizationManager = BeanLoader.getBean("Gradf_Foura_DefaultAuthorizationManager");
    private IAuthenticationManager authenticationManager = BeanLoader.getBean("Gradf_Foura_DefaultAuthenticationManager");

    public void putOperator(Long operator) {
        ContextUtil.INSTANCE.setOperator(operator);
    }

    public void setOperatorName(String operatorName) {
        ContextUtil.INSTANCE.setOperatorName(operatorName);
    }

    public Long getOperator() {
        return ContextUtil.INSTANCE.getOperator();
    }

    public String getOperatorName() {
        return ContextUtil.INSTANCE.getOperatorName();
    }

    public AccountPasswordResultDomain authenticationInfoByKey(String accountKey) {
        return authenticationManager.authenticationInfoByKey(accountKey);
    }

    public AccountPasswordResultDomain authenticationInfoByJwt(String jwtString) {
        Jws<Claims> parse = AccessTokenUtil.INSTANCE.parse(jwtString);
        Claims claims = parse.getBody();
        Long account_id = claims.get("account_id", Long.class);
        AccountPasswordResultDomain accountPassword = authenticationManager.authenticationInfoById(account_id);
        AccessTokenUtil.INSTANCE.validate(parse, accountPassword.getJwt_key());
        return accountPassword;
    }

    public Set<String> permissionsByAccountId(Long account_id) {
        return authorizationManager.permissionsByAccountId(account_id);
    }

    public Set<String> rolesByAccountId(Long account_id) {
        return authorizationManager.roleKeysByAccountId(account_id);
    }
}
