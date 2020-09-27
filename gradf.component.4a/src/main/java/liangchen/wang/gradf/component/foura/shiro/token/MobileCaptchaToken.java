package liangchen.wang.gradf.component.foura.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiangChen.Wang
 */
public class MobileCaptchaToken implements AuthenticationToken {
    private String mobile;
    private String captchaKey;
    private String captcha;

    public MobileCaptchaToken(String mobile, String captchaKey, String captcha) {
        this.mobile = mobile;
        this.captchaKey = captchaKey;
        this.captcha = captcha;
    }

    @Override
    public Object getPrincipal() {
        return mobile;
    }

    @Override
    public Object getCredentials() {
        Map<String, String> map = new HashMap<>(2);
        map.put("captchaKey", captchaKey);
        map.put("captcha", captcha);
        return map;
    }

}