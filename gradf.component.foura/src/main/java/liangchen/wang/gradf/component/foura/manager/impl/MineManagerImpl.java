package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.foura.dao.query.RoleQuery;
import liangchen.wang.gradf.component.foura.manager.IAccountManager;
import liangchen.wang.gradf.component.foura.manager.IMineManager;
import liangchen.wang.gradf.component.foura.manager.IRoleAccountManager;
import liangchen.wang.gradf.component.foura.manager.IRoleManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.ModifyPasswordParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.AccountLoginResultDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.AccountResultDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleResultDomain;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component("Gradf_Foura_DefaultMineManager")
public class MineManagerImpl implements IMineManager {
    private final IAccountManager accountManager;
    private final IRoleManager roleManager;
    private final IRoleAccountManager roleAccountManager;

    @Inject
    public MineManagerImpl(@Named("Gradf_Foura_DefaultAccountManager") IAccountManager accountManager,
                           @Named("Gradf_Foura_DefaultRoleManager") IRoleManager roleManager,
                           @Named("Gradf_Foura_DefaultRoleAccountManager") IRoleAccountManager roleAccountManager) {
        this.accountManager = accountManager;
        this.roleManager = roleManager;
        this.roleAccountManager = roleAccountManager;
    }

    @Override
    public AccountLoginResultDomain whoAmI(Long operator) {
        AccountResultDomain accountResultDomain = accountManager.byPrimaryKeyOrThrow(operator, new String[]{Status.NORMAL.name()}, null, "account_id", "login_name", "mobile", "email", "nick_name", "last_login", "status");
        AccountLoginResultDomain accountLoginResultDomain = accountResultDomain.copyTo(AccountLoginResultDomain.class);
        Set<Long> roleIds = roleAccountManager.roleIdsByAccountId(operator);
        RoleQuery roleQuery = RoleQuery.newInstance();
        roleQuery.setRoleIdIn(roleIds);
        List<RoleResultDomain> roleResultDomains = roleManager.list(roleQuery, "role_key,role_text");
        List<AccountLoginResultDomain.Role> roles = roleResultDomains.stream().map(e -> new AccountLoginResultDomain.Role(e.getRole_key(), e.getRole_text())).collect(Collectors.toList());
        accountLoginResultDomain.setRoles(roles);
        return accountLoginResultDomain;
    }

    @Override
    public void modifyPassword(ModifyPasswordParameterDomain parameter) {
        accountManager.modifyPassword(parameter);
    }
}
