package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.foura.dao.query.UrlRelationQuery;
import liangchen.wang.gradf.component.foura.enumeration.FouraStatus;
import liangchen.wang.gradf.component.foura.exception.DuplicateAccountException;
import liangchen.wang.gradf.component.foura.initialization.IFouraInitialization;
import liangchen.wang.gradf.component.foura.initialization.domain.*;
import liangchen.wang.gradf.component.foura.manager.*;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.*;
import liangchen.wang.gradf.component.foura.manager.domain.result.UrlRelationResultDomain;
import liangchen.wang.gradf.component.foura.shiro.utils.ShiroFilterChainUtil;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.utils.Printer;
import liangchen.wang.gradf.framework.data.annotation.EnableJdbcShedLock;
import liangchen.wang.gradf.framework.data.enumeration.DataMode;
import net.javacrumbs.shedlock.core.DefaultLockingTaskExecutor;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.LockingTaskExecutor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Instant;
import java.util.*;

@Service("Gradf_Foura_DefaultInitializationService")
@EnableJdbcShedLock
public class InitializationManagerImpl implements IInitializationManager {
    private final List<IFouraInitialization> customizedDatas;
    private final IGroupManager groupManager;
    private final IRoleManager roleManager;
    private final IAccountManager accountManager;
    private final IGroupAccountManager groupAccountManager;
    private final IRoleAccountManager roleAccountManager;
    private final IResourceManager resourceManager;
    private final IOperationManager operationManager;
    private final IRoleResourcePrivilegeManager roleResourcePrivilegeManager;
    private final IRoleResourceOperationManager roleResourceOperationManager;
    private final IUrlManager urlManager;
    private final IUrlRelationManager urlRelationManager;
    private final LockProvider lockProvider;

    @Inject
    public InitializationManagerImpl(List<IFouraInitialization> customizedDatas,
                                     @Named("Gradf_Foura_DefaultGroupManager") IGroupManager groupManager,
                                     @Named("Gradf_Foura_DefaultRoleManager") IRoleManager roleManager,
                                     @Named("Gradf_Foura_DefaultAccountManager") IAccountManager accountManager,
                                     @Named("Gradf_Foura_DefaultGroupAccountManager") IGroupAccountManager groupAccountManager,
                                     @Named("Gradf_Foura_DefaultRoleAccountManager") IRoleAccountManager roleAccountManager,
                                     @Named("Gradf_Foura_DefaultResourceManager") IResourceManager resourceManager,
                                     @Named("Gradf_Foura_DefaultOperationManager") IOperationManager operationManager,
                                     @Named("Gradf_Foura_DefaultRoleResourcePrivilegeManager") IRoleResourcePrivilegeManager roleResourcePrivilegeManager,
                                     @Named("Gradf_Foura_DefaultRoleResourceOperationManager") IRoleResourceOperationManager roleResourceOperationManager,
                                     @Named("Gradf_Foura_DefaultUrlManager") IUrlManager urlManager,
                                     @Named("Gradf_Foura_DefaultUrlRelationManager") IUrlRelationManager urlRelationManager,
                                     LockProvider lockProvider) {
        this.customizedDatas = customizedDatas;
        this.groupManager = groupManager;
        this.roleManager = roleManager;
        this.accountManager = accountManager;
        this.groupAccountManager = groupAccountManager;
        this.roleAccountManager = roleAccountManager;
        this.resourceManager = resourceManager;
        this.operationManager = operationManager;
        this.roleResourcePrivilegeManager = roleResourcePrivilegeManager;
        this.roleResourceOperationManager = roleResourceOperationManager;
        this.urlManager = urlManager;
        this.urlRelationManager = urlRelationManager;
        this.lockProvider = lockProvider;
    }

    @Override
    public void initFouraData() {
        LockingTaskExecutor executor = new DefaultLockingTaskExecutor(lockProvider);
        Instant lockAtMostUntil = Instant.now().plusSeconds(3600);
        Instant lockAtLeastUntil = Instant.now().plusSeconds(1800);
        executor.executeWithLock((Runnable) () -> {
            Set<GroupInitialization> groups = new HashSet<>();
            Set<RoleInitialization> roles = new HashSet<>();
            Set<AccountInitialization> accounts = new HashSet<>();
            List<GroupRolesInitialization> groupRoles = new ArrayList<>();
            List<GroupAccountsInitialization> groupAccounts = new ArrayList<>();
            List<RoleAccountsInitialization> roleAccounts = new ArrayList<>();
            customizedDatas.forEach(e -> {
                groups.addAll(e.groups());
                roles.addAll(e.roles());
                accounts.addAll(e.accounts());
                groupRoles.add(e.groupRoles());
                groupAccounts.add(e.groupAccounts());
                roleAccounts.add(e.roleAccounts());
            });
            initGroups(groups);
            initRoles(roles);
            initAccounts(accounts);
            initGroupRoles(groupRoles);
            initGroupAccounts(groupAccounts);
            initRoleAccounts(roleAccounts);
        }, new LockConfiguration("FouraInitializationLock", lockAtMostUntil, lockAtLeastUntil));
    }


    @Override
    public void initAuth() {
        List<UrlRolesInitialization> urlRoles = new ArrayList<>();
        customizedDatas.forEach(e -> urlRoles.add(e.urlRoles()));
        initUrlRolesAndPermissions(urlRoles);
    }

    private void initGroups(Set<GroupInitialization> groupInitializations) {
        Printer.INSTANCE.prettyPrint("初始化预置群组开始");
        if (CollectionUtil.INSTANCE.isEmpty(groupInitializations)) {
            Printer.INSTANCE.prettyPrint("未设置预置群组,无需初始化");
            return;
        }
        groupInitializations.forEach(g -> {
            GroupParameterDomain groupParameterDomain = GroupParameterDomain.newInstance();
            String groupKey = g.getGroupKey();
            String groupText = g.getGroupText();
            groupParameterDomain.setGroup_key(g.getGroupKey());
            groupParameterDomain.setGroup_text(g.getGroupText());
            groupParameterDomain.setData_mode(DataMode.N.getValue());
            try {
                groupManager.insert(groupParameterDomain);
                Printer.INSTANCE.prettyPrint("{}({})初始化完成", groupText, groupKey);
            } catch (DuplicateKeyException e) {
                Printer.INSTANCE.prettyPrint("{}({})已存在", groupText, groupKey);
            }
        });
        Printer.INSTANCE.prettyPrint("初始化预置群组完成");
    }

    private void initRoles(Set<RoleInitialization> roleInitializations) {
        Printer.INSTANCE.prettyPrint("初始化预置角色开始");
        if (CollectionUtil.INSTANCE.isEmpty(roleInitializations)) {
            Printer.INSTANCE.prettyPrint("未设置预置角色,无需初始化");
            return;
        }
        roleInitializations.forEach(r -> {
            RoleParameterDomain roleParameterDomain = RoleParameterDomain.newInstance();
            String roleKey = r.getRoleKey();
            String roleText = r.getRoleText();
            roleParameterDomain.setRole_key(roleKey);
            roleParameterDomain.setRole_text(roleText);
            roleParameterDomain.setData_mode(DataMode.N.getValue());
            try {
                roleManager.insert(roleParameterDomain);
                Printer.INSTANCE.prettyPrint("{}({})初始化完成", roleText, roleKey);
            } catch (DuplicateKeyException e) {
                Printer.INSTANCE.prettyPrint("{}({})已存在", roleText, roleKey);
            }
        });
        Printer.INSTANCE.prettyPrint("初始化预置角色完成");
    }

    private void initAccounts(Set<AccountInitialization> accountInitializations) {
        Printer.INSTANCE.prettyPrint("初始化预置账户开始");
        if (CollectionUtil.INSTANCE.isEmpty(accountInitializations)) {
            Printer.INSTANCE.prettyPrint("未设置预置账户,无需初始化");
            return;
        }
        accountInitializations.forEach(a -> {
            AccountParameterDomain accountParameterDomain = AccountParameterDomain.newInstance();
            String nickName = a.getNickName();
            String loginName = a.getLoginName();
            accountParameterDomain.setLogin_name(loginName);
            accountParameterDomain.setNick_name(nickName);
            accountParameterDomain.setLogin_password(a.getPassword());
            accountParameterDomain.setData_mode(DataMode.N.getValue());
            try {
                accountManager.insert(accountParameterDomain);
                Printer.INSTANCE.prettyPrint("{}({})初始化完成", nickName, loginName);
            } catch (DuplicateAccountException e) {
                Printer.INSTANCE.prettyPrint("{}({})已存在", nickName, loginName);
            }
        });
        Printer.INSTANCE.prettyPrint("初始化系统账户完成");
    }

    private void initGroupRoles(List<GroupRolesInitialization> groupRoles) {
    }

    private void initGroupAccounts(List<GroupAccountsInitialization> groupAccounts) {
        Printer.INSTANCE.prettyPrint("初始化系统群组账户关系开始");
        // 构造groupAccountsMap
        Map<String, Set<String>> groupAccountsMap = new HashMap<>();
        groupAccounts.forEach(ga -> {
            Map<String, Set<String>> setMap = ga.get();
            setMap.forEach((k, v) -> {
                Set<String> value = groupAccountsMap.putIfAbsent(k, v);
                if (null != value) {
                    value.addAll(v);
                }
            });
        });
        if (CollectionUtil.INSTANCE.isEmpty(groupAccountsMap)) {
            Printer.INSTANCE.prettyPrint("未设置系统群组账户关系,无需初始化");
            return;
        }
        groupAccountsMap.forEach((groupKey, loginNames) -> {
            Long groupId = groupManager.idByKey(groupKey);
            loginNames.forEach(loginName -> {
                Long accountId = accountManager.idByKey(loginName);
                GroupAccountParameterDomain groupAccountParameterDomain = GroupAccountParameterDomain.newInstance();
                groupAccountParameterDomain.setAccount_id(accountId);
                groupAccountParameterDomain.setGroup_id(groupId);
                try {
                    groupAccountManager.insert(groupAccountParameterDomain);
                    Printer.INSTANCE.prettyPrint("{}-{}初始化完成", groupKey, loginName);
                } catch (DuplicateKeyException e) {
                    Printer.INSTANCE.prettyPrint("{}-{}已存在", groupKey, loginName);
                }
            });
        });
        Printer.INSTANCE.prettyPrint("初始化系统群组账户关系完成");
    }

    private void initRoleAccounts(List<RoleAccountsInitialization> roleAccounts) {
        Printer.INSTANCE.prettyPrint("初始化系统角色账户关系开始");
        // 构造roleAccountsMap
        Map<String, Set<String>> roleAccountsMap = new HashMap<>();
        roleAccounts.forEach(ra -> {
            Map<String, Set<String>> setMap = ra.get();
            setMap.forEach((k, v) -> {
                Set<String> value = roleAccountsMap.putIfAbsent(k, v);
                if (null != value) {
                    value.addAll(v);
                }
            });
        });
        if (CollectionUtil.INSTANCE.isEmpty(roleAccountsMap)) {
            Printer.INSTANCE.prettyPrint("未设置系统角色账户关系,无需初始化");
            return;
        }
        roleAccountsMap.forEach((roleKey, loginNames) -> {
            Long roleId = roleManager.idByKey(roleKey);
            loginNames.forEach(loginName -> {
                Long accountId = accountManager.idByKey(loginName);
                RoleAccountParameterDomain roleAccountParameterDomain = RoleAccountParameterDomain.newInstance();
                roleAccountParameterDomain.setAccount_id(accountId);
                roleAccountParameterDomain.setRole_id(roleId);
                try {
                    roleAccountManager.insert(roleAccountParameterDomain);
                    Printer.INSTANCE.prettyPrint("{}-{}初始化完成", roleKey, loginName);
                } catch (DuplicateKeyException e) {
                    Printer.INSTANCE.prettyPrint("{}-{}已存在", roleKey, loginName);
                }
            });
        });
        Printer.INSTANCE.prettyPrint("初始化系统角色账户关系完成");
    }

    private void initUrlRolesAndPermissions(List<UrlRolesInitialization> urlRoles) {
        Printer.INSTANCE.prettyPrint("初始化系统Url角色关系开始");
        // 构造groupAccountsMap
        Map<String, Set<String>> urlRolesMap = new HashMap<>();
        urlRoles.forEach(ur -> {
            Map<String, Set<String>> setMap = ur.get();
            setMap.forEach((k, v) -> {
                Set<String> value = urlRolesMap.putIfAbsent(k, v);
                if (null != value) {
                    value.addAll(v);
                }
            });
        });
        // 查询配置的url_role关系
        List<UrlRelationResultDomain> urlRelations = urlRelationManager.list(UrlRelationQuery.newInstance(), "role_id,url_id");
        urlRelations.forEach(urlRelation -> {
            String roleKey = roleManager.keyById(urlRelation.getRole_id());
            String urlPath = urlManager.urlPathById(urlRelation.getUrl_id());
            Set<String> roleKeys = CollectionUtil.INSTANCE.array2Set(new String[]{roleKey});
            Set<String> value = urlRolesMap.putIfAbsent(urlPath, roleKeys);
            if (null != value) {
                value.addAll(roleKeys);
            }
        });
        urlRolesMap.forEach((urlPath, roleKeys) -> {
            if (CollectionUtil.INSTANCE.isEmpty(roleKeys)) {
                ShiroFilterChainUtil.addLoginToChain(urlPath);
                Printer.INSTANCE.prettyPrint("初始化登录即可访问url:{}", urlPath);
            } else {
                String roleKeysSplit = String.join(",", roleKeys);
                ShiroFilterChainUtil.addRolesAndPermissionsToChain(urlPath, roleKeysSplit);
                Printer.INSTANCE.prettyPrint("初始化角色可访问url:{},role:{}", urlPath, roleKeysSplit);
            }
        });
        Printer.INSTANCE.prettyPrint("初始化系统Url角色关系完成");
    }
}
