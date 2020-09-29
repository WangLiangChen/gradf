package liangchen.wang.gradf.component.foura.controller;


import liangchen.wang.gradf.component.foura.manager.IAccountManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author LiangChen.Wang 2019/7/5 15:42
 */
@RestController("Gradf_Foura_DefaultAuthedAccountController")
@RequestMapping("/auth/gradf/4a/account")
public class AuthedAccountController {

    private final IAccountManager manager;

    @Inject
    public AuthedAccountController(@Named("Gradf_Foura_DefaultAccountManager") IAccountManager manager) {
        this.manager = manager;
    }
}
