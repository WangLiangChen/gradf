package liangchen.wang.gradf.component.foura.manager.domain.parameter;

import javax.validation.constraints.NotEmpty;

/**
 * @author LiangChen.Wang 2019/7/5 15:50
 */
public class UsernamePasswordLoginParameterDomain {
    @NotEmpty(message = "登录名不能为空")
    private String username;
    @NotEmpty(message = "登录密码不能为空")
    private String password;
    private String captchaKey;
    private String captcha;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public void setCaptchaKey(String captchaKey) {
        this.captchaKey = captchaKey;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
