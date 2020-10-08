package liangchen.wang.gradf.component.foura.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author LiangChen.Wang
 */
public class JwtToken implements AuthenticationToken {
    private String access_token;

    public JwtToken(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public Object getPrincipal() {
        return access_token;
    }

    @Override
    public Object getCredentials() {
        return access_token;
    }

}