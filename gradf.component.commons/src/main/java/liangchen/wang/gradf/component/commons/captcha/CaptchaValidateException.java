package liangchen.wang.gradf.component.commons.captcha;

import liangchen.wang.gradf.framework.commons.exception.GradfException;

/**
 * @author LiangChen.Wang 2019/7/5 8:58
 */
public class CaptchaValidateException extends GradfException {
    public CaptchaValidateException(String message, Object... args) {
        super(String.format(message.replaceAll("\\{}", "\\%s"), args));
    }

    public CaptchaValidateException(Throwable throwable) {
        super(throwable);
    }
}
