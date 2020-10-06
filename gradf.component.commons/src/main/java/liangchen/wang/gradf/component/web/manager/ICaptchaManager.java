package liangchen.wang.gradf.component.web.manager;

import liangchen.wang.gradf.component.web.manager.domain.parameter.CaptchaParameterDomain;
import liangchen.wang.gradf.component.web.manager.domain.parameter.CaptchaRefreshDomain;
import liangchen.wang.gradf.component.web.manager.domain.parameter.CaptchaValidateDomain;
import liangchen.wang.gradf.component.web.manager.domain.result.CaptchaResultDomain;

/**
 * @author LiangChen.Wang 2019/6/28 15:10
 */
public interface ICaptchaManager {
    CaptchaResultDomain createCaptcha(CaptchaParameterDomain parameterDomain);

    void validate(String key, String code, String business_type);

    void validate(String key, String code, String business_type, String business_data, boolean deleteOnSuccess);

    CaptchaResultDomain refresh(CaptchaRefreshDomain refreshDomain);

    void validate(CaptchaValidateDomain validateDomain);
}
