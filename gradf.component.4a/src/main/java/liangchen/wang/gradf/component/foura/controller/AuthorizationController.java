package liangchen.wang.gradf.component.foura.controller;

import liangchen.wang.gradf.component.foura.manager.IAuthorizationManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.AuthUrlParameterDomain;
import liangchen.wang.gradf.framework.webmvc.result.ResponseUtil;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

/**
 * @author LiangChen.Wang 2019/8/7 13:40
 */
@RestController
@RequestMapping("/auth/crdf/4a/authorization")
public class AuthorizationController {
    private final IAuthorizationManager manager;

    @Inject
    public AuthorizationController(@Named("Crdf_Foura_DefaultAuthorizationManager") IAuthorizationManager manager) {
        this.manager = manager;
    }

    @PostMapping("/validateAuthWithUrl")
    public void validateAuthWithUrl(@RequestBody List<AuthUrlParameterDomain> parameters) {
        Map<String, Boolean> results = manager.validateAuthWithUrl(parameters);
        ResponseUtil.createResponse().data(results).flush();
    }

    @GetMapping("/isLogin")
    public void isLogin() {
    }
}
