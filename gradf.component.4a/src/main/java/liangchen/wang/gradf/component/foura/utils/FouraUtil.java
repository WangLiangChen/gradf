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
    private final String OPERATOR = "OPERATOR";
    private final String OPERATOR_NAME = "OPERATOR_NAME";

    public void putOperator(Long operator) {
        ContextUtil.INSTANCE.put(OPERATOR, operator);
    }

    public void putOperatorName(String operatorName) {
        ContextUtil.INSTANCE.put(OPERATOR_NAME, operatorName);
    }

    public Long getOperator() {
        return ContextUtil.INSTANCE.get(OPERATOR);
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

    public Set<String> roleIdsByAccountId(Long account_id) {
        return authorizationManager.roleIdsByAccountId(account_id);
    }

    public Set<String> permissionsByAccountId(Long account_id) {
        return authorizationManager.permissionsByAccountId(account_id);
    }
}
