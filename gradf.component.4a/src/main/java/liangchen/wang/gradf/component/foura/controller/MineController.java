package liangchen.wang.gradf.component.foura.controller;

import liangchen.wang.gradf.component.foura.manager.IMineManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.ModifyPasswordParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.AccountLoginResultDomain;
import liangchen.wang.gradf.component.foura.utils.FouraUtil;
import liangchen.wang.gradf.framework.webmvc.result.ResponseUtil;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author LiangChen.Wang 2019/7/22 15:05
 */
@RestController
@RequestMapping("/auth/crdf/4a/mine")
public class MineController {
    private final IMineManager manager;

    @Inject
    public MineController(@Named("Crdf_Foura_DefaultMineManager") IMineManager manager) {
        this.manager = manager;
    }

    @GetMapping("/whoAmI")
    public void whoAmI() {
        Long operator = FouraUtil.INSTANCE.getOperator();
        AccountLoginResultDomain resultDomain = manager.whoAmI(operator);
        ResponseUtil.createResponse().data(resultDomain).flush();
    }

    @PostMapping("modifyPassword")
    public void modifyPassword(@RequestBody ModifyPasswordParameterDomain parameter) {
        manager.modifyPassword(parameter);
    }
}
