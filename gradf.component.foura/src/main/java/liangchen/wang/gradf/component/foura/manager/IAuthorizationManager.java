package liangchen.wang.gradf.component.foura.manager;

import liangchen.wang.gradf.component.foura.manager.domain.parameter.AuthUrlParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleResultDomain;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author LiangChen.Wang
 */
public interface IAuthorizationManager {

    List<RoleResultDomain> rolesByAccountId(Long account_id, String... returnFields);

    Map<String, Boolean> validateAuthWithUrl(List<AuthUrlParameterDomain> parameters);

    Set<String> subjectUrls();

    void assign2Role(Long accountId, String roleKey);

    Set<String> permissionsByAccountId(Long account_id);

    Set<String> roleKeysByAccountId(Long account_id);
}
