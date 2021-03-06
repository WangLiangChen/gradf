package liangchen.wang.gradf.component.foura.controller;


import liangchen.wang.gradf.component.foura.manager.IAuthenticationManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.UsernamePasswordLoginParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.AccountLoginResultDomain;
import liangchen.wang.gradf.framework.web.result.ResponseUtil;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * @author LiangChen.Wang 2019/7/5 15:42
 * 账号Account
 * 认证Authentication
 * 授权Authorization
 * 审计Audit
 */
@RestController("Gradf_Foura_DefaultAuthenticationController")
@RequestMapping(value = {"/gradf/foura/authentication", "/auth/gradf/foura/authentication"})
public class AuthenticationController {

    private final IAuthenticationManager manager;

    @Inject
    public AuthenticationController(@Named("Gradf_Foura_DefaultAuthenticationManager") IAuthenticationManager manager) {
        this.manager = manager;
    }

    @PostMapping("/loginWithUsernamePassword")
    public void loginWithUsernamePassword(@RequestBody UsernamePasswordLoginParameterDomain parameterDomain, HttpServletRequest request) {
        AccountLoginResultDomain login = manager.loginWithUsernamePassword(parameterDomain);
        ResponseUtil.createResponse().data(login).flush();
    }

    @GetMapping("/logout")
    public void logout() {
    }

    @GetMapping("/isLogin")
    public void isLogin() {
    }
}
