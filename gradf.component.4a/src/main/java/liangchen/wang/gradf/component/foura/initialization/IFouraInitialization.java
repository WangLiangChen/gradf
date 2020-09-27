package liangchen.wang.gradf.component.foura.initialization;

import liangchen.wang.gradf.component.foura.initialization.domain.*;

import java.util.Set;

/**
 * @author .LiangChen.Wang
 */
public interface IFouraInitialization {

    Set<GroupInitialization> groups();

    Set<RoleInitialization> roles();

    Set<AccountInitialization> accounts();

    GroupAccountsInitialization groupAccounts();

    RoleAccountsInitialization roleAccounts();

    UrlRolesInitialization urlRoles();
}
