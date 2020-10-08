package liangchen.wang.gradf.component.foura.initialization.impl;

import liangchen.wang.gradf.component.foura.enumeration.FouraDefaultRoles;
import liangchen.wang.gradf.component.foura.initialization.IFouraInitialization;
import liangchen.wang.gradf.component.foura.initialization.domain.*;
import liangchen.wang.gradf.framework.web.enumeration.Constant;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author .LiangChen.Wang
 */
@Component("Gradf_Foura_DefaultFouraInitialization")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultFouraInitializationImpl implements IFouraInitialization {
    @Override
    public Set<GroupInitialization> groups() {
        return Collections.emptySet();
    }

    @Override
    public Set<RoleInitialization> roles() {
        FouraDefaultRoles[] values = FouraDefaultRoles.values();
        Set<RoleInitialization> set = new HashSet<>(values.length);
        for (FouraDefaultRoles value : values) {
            set.add(RoleInitialization.newInstance(value.name(), value.getText()));
        }
        return set;
    }

    @Override
    public Set<AccountInitialization> accounts() {
        Set<AccountInitialization> set = new HashSet<>(2);
        set.add(AccountInitialization.newInstance("root", "超级管理员", "Root#123456"));
        set.add(AccountInitialization.newInstance("admin", "管理员", "Admin#123456"));
        return set;
    }

    @Override
    public GroupRolesInitialization groupRoles() {
        return GroupRolesInitialization.newInstance();
    }

    @Override
    public GroupAccountsInitialization groupAccounts() {
        return GroupAccountsInitialization.newInstance();
    }

    @Override
    public RoleAccountsInitialization roleAccounts() {
        RoleAccountsInitialization roleAccountsInitialization = RoleAccountsInitialization.newInstance();
        roleAccountsInitialization.put(FouraDefaultRoles.ROOT.name(), "root");
        roleAccountsInitialization.put(FouraDefaultRoles.ADMIN.name(), "admin");
        return roleAccountsInitialization;
    }

    @Override
    public UrlRolesInitialization urlRoles() {
        UrlRolesInitialization urlRolesInitialization = UrlRolesInitialization.newInstance();
        urlRolesInitialization.put(Constant.Path.AUTH.getPath("gradf/foura/mine/**"));
        urlRolesInitialization.put(Constant.Path.AUTH.getPath("gradf/foura/authorization/validateAuthWithUrl"));
        urlRolesInitialization.put(Constant.Path.AUTH.getPath("**"), FouraDefaultRoles.ROOT.name());
        return urlRolesInitialization;
    }

}
