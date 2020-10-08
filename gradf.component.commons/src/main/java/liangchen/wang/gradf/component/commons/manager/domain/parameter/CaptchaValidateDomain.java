package liangchen.wang.gradf.component.commons.manager.domain.parameter;

import javax.validation.constraints.NotEmpty;

/**
 * @author LiangChen.Wang 2019/7/5 10:43
 */
public class CaptchaValidateDomain {
    @NotEmpty(message = "业务类型不能为空")
    private String business_type;
    private String business_data;
    @NotEmpty(message = "验证码Key不能为空")
    private String captcha_key;
    @NotEmpty(message = "验证码不能为空")
    private String captcha;

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getBusiness_data() {
        return business_data;
    }

    public void setBusiness_data(String business_data) {
        this.business_data = business_data;
    }

    public String getCaptcha_key() {
        return captcha_key;
    }

    public void setCaptcha_key(String captcha_key) {
        this.captcha_key = captcha_key;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
