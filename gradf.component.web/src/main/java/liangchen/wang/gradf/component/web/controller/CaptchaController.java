package liangchen.wang.gradf.component.web.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * @author LiangChen.Wang
 */
@RestController
@RequestMapping(value = "/crdf/captcha")
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
