package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.manager.domain.parameter.UsernamePasswordLoginParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.AccountLoginResultDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.AccountPasswordResultDomain;

public interface IAuthenticationManager {
    AccountPasswordResultDomain authenticationInfoByKey(String accountKey);

    AccountLoginResultDomain loginWithUsernamePassword(UsernamePasswordLoginParameterDomain parameterDomain);

    AccountPasswordResultDomain authenticationInfoById(Long account_id);
}
