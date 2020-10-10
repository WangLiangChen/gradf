package liangchen.wang.gradf.component.commons.controller;


import liangchen.wang.gradf.component.commons.manager.ICaptchaManager;
import liangchen.wang.gradf.component.commons.manager.domain.parameter.CaptchaParameterDomain;
import liangchen.wang.gradf.component.commons.manager.domain.parameter.CaptchaRefreshDomain;
import liangchen.wang.gradf.component.commons.manager.domain.parameter.CaptchaValidateDomain;
import liangchen.wang.gradf.component.commons.manager.domain.result.CaptchaResultDomain;
import liangchen.wang.gradf.framework.web.result.ResponseUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * @author LiangChen.Wang
 */
@RestController
@RequestMapping(value = "/captcha")
public class CaptchaController {
    private final ICaptchaManager manager;

    @Inject
    public CaptchaController(ICaptchaManager manager) {
        this.manager = manager;
    }

    @PostMapping("/image")
    public void image(@RequestBody CaptchaParameterDomain parameterDomain) {
        CaptchaResultDomain captcha = manager.createCaptcha(parameterDomain);
        ResponseUtil.createResponse().data(captcha).flush();
    }

    @PostMapping("refresh")
    public void refresh(@RequestBody CaptchaRefreshDomain refreshDomain) {
        CaptchaResultDomain captcha = manager.refresh(refreshDomain);
        ResponseUtil.createResponse().data(captcha).flush();
    }

    @PostMapping("validate")
    public void validate(@RequestBody CaptchaValidateDomain validateDomain) {
        manager.validate(validateDomain);
    }
}
