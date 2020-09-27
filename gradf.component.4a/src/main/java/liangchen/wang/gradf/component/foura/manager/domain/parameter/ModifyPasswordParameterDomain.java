package liangchen.wang.gradf.component.foura.manager.domain.parameter;

import javax.validation.constraints.NotEmpty;

/**
 * @author LiangChen.Wang 2019/7/5 15:50
 */
public class ModifyPasswordParameterDomain {
    @NotEmpty(message = "原密码不能为空")
    private String original_password;
    @NotEmpty(message = "新密码不能为空")
    private String new_password;
    @NotEmpty(message = "重复密码不能为空")
    private String repeat_password;
    private String captchaKey;
    private String captcha;

    public String getOriginal_password() {
        return original_password;
    }

    public void setOriginal_password(String original_password) {
        this.original_password = original_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getRepeat_password() {
        return repeat_password;
    }

    public void setRepeat_password(String repeat_password) {
        this.repeat_password = repeat_password;
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
