package liangchen.wang.gradf.component.web.manager.domain.result;

/**
 * @author LiangChen.Wang 2019/7/5 10:43
 */
public class CaptchaResultDomain {
    private String captcha_key;
    private String captcha_base64;

    public static CaptchaResultDomain newInstance() {
        return new CaptchaResultDomain();
    }

    public String getCaptcha_key() {
        return captcha_key;
    }

    public void setCaptcha_key(String captcha_key) {
        this.captcha_key = captcha_key;
    }

    public String getCaptcha_base64() {
        return captcha_base64;
    }

    public void setCaptcha_base64(String captcha_base64) {
        this.captcha_base64 = captcha_base64;
    }
}
