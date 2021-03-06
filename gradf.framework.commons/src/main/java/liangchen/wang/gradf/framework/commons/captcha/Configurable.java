package liangchen.wang.gradf.framework.commons.captcha;

/**
 * @author LiangChen.Wang 2019/7/3 16:03
 */
public abstract class Configurable {

    private CaptchaProperties properties;

    public CaptchaProperties getProperties() {
        return properties;
    }

    public void setProperties(CaptchaProperties properties) {
        this.properties = properties;
    }

}
