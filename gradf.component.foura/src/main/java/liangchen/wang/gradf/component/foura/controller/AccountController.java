package liangchen.wang.gradf.component.foura.controller;


import liangchen.wang.gradf.component.foura.manager.IAccountManager;
import liangchen.wang.gradf.framework.web.result.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author LiangChen.Wang 2019/7/5 15:42
 */
@RestController("Gradf_Foura_DefaultAccountController")
@RequestMapping("/gradf/foura/account")
public class AccountController {
    private final IAccountManager manager;

    @Inject
    public AccountController(@Named("Gradf_Foura_DefaultAccountManager") IAccountManager manager) {
        this.manager = manager;
    }

    @GetMapping("/validateExist")
    public void validateExist(String accountKey) {
        manager.validateExist(accountKey);
    }

    @GetMapping("/exist")
    public void exist(String accountKey) {
        boolean exist = manager.exist(accountKey);
        ResponseUtil.createResponse().data(exist).flush();
    }

}
