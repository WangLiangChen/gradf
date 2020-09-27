package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.manager.domain.parameter.ModifyPasswordParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.AccountLoginResultDomain;

public interface IMineManager {
    AccountLoginResultDomain whoAmI(Long operator);

    void modifyPassword(ModifyPasswordParameterDomain parameter);
}
