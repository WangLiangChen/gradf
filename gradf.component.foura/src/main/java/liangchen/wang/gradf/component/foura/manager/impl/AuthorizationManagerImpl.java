package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.foura.dao.query.RoleQuery;
import liangchen.wang.gradf.component.foura.dao.query.RoleResourceOperationQuery;
import liangchen.wang.gradf.component.foura.dao.query.RoleResourcePrivilegeQuery;
import liangchen.wang.gradf.component.foura.manager.*;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.AuthUrlParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.RoleAccountParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleResourceOperationResultDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleResourcePrivilegeResultDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleResultDomain;
import liangchen.wang.gradf.component.foura.shiro.filter.StatelessFilter;
import liangchen.wang.gradf.component.foura.shiro.utils.ShiroUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.Filter;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author LiangChen.Wang
 */
@Component("Gradf_Foura_DefaultAuthorizationManager")
public class AuthorizationManagerImpl implements IAuthorizationManager {
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final IRoleAccountManager roleAccountManager;
    private final IRoleManager roleManager;
    private final IAccountManager accountManager;
    private final IResourceManager resourceManager;
    private final IOperationManager operationManager;
    private final IRoleResourceOperationManager roleResourceOperationManager;
    private final IRoleResourcePrivilegeManager roleResourcePrivilegeManager;

    @Inject
    public AuthorizationManagerImpl(@Named("Gradf_Foura_DefaultRoleAccountManager") IRoleAccountManager roleAccountManager,
                                    @Named("Gradf_Foura_DefaultRoleManager") IRoleManager roleManager,
                                    @Named("Gradf_Foura_DefaultAccountManager") IAccountManager accountManager,
                                    @Named("Gradf_Foura_DefaultResourceManager") IResourceManager resourceManager,
                                    @Named("Gradf_Foura_DefaultOperationManager") IOperationManager operationManager,
                                    @Named("Gradf_Foura_DefaultRoleResourceOperationManager") IRoleResourceOperationManager roleResourceOperationManager,
                                    @Named("Gradf_Foura_DefaultRoleResourcePrivilegeManager") IRoleResourcePrivilegeManager roleResourcePrivilegeManager) {
        this.roleAccountManager = roleAccountManager;
        this.roleManager = roleManager;
        this.accountManager = accountManager;
        this.resourceManager = resourceManager;
        this.operationManager = operationManager;
        this.roleResourceOperationManager = roleResourceOperationManager;
        this.roleResourcePrivilegeManager = roleResourcePrivilegeManager;
    }

    @Override
    public List<RoleResultDomain> rolesByAccountId(Long account_id, String... returnFields) {
        Assert.INSTANCE.notNull(account_id, "账户ID不能为空");
        // 根据account_id查询角色
        Set<Long> roleIds = roleAccountManager.roleIdsByAccountId(account_id);
        RoleQuery query = RoleQuery.newInstance();
        query.setRoleIdIn(roleIds);
        return roleManager.list(query, returnFields);
    }

    @Override
    public Set<String> roleKeysByAccountId(Long account_id) {
        Assert.INSTANCE.notNull(account_id, "账户ID不能为空");
        // 根据account_id查询角色
        Set<Long> roleIds = roleAccountManager.roleIdsByAccountId(account_id);
        return roleIds.stream().map(e -> roleManager.keyById(e)).collect(Collectors.toSet());
    }

    @Override
    public Set<String> permissionsByAccountId(Long account_id) {
        Set<String> permissions = new HashSet<>();
        // 获取角色
        Set<Long> roleIds = roleAccountManager.roleIdsByAccountId(account_id);
        // 获取角色/资源/操作
        RoleResourceOperationQuery roleResourceOperationQuery = RoleResourceOperationQuery.newInstance();
        roleResourceOperationQuery.setRoleIdIn(roleIds);
        List<RoleResourceOperationResultDomain> roleResourceOperations = roleResourceOperationManager.list(roleResourceOperationQuery, "resource_id,operation_id");
        // 获取 资源Key/操作Key
        roleResourceOperations.forEach(e -> {
            String resourceKey = resourceManager.keyById(e.getResource_id());
            String operationKey = operationManager.keyById(e.getOperation_id());
            permissions.add(String.format("%s:%s", resourceKey, operationKey));
        });
        /****************************BitPermission************************************/
        RoleResourcePrivilegeQuery roleResourcePrivilegeQuery = RoleResourcePrivilegeQuery.newInstance();
        roleResourcePrivilegeQuery.setRoleIdIn(roleIds);
        List<RoleResourcePrivilegeResultDomain> roleResourcePrivileges = roleResourcePrivilegeManager.list(roleResourcePrivilegeQuery, "resource_id,privilege");
        //+资源字符串+权限位+实例ID
        roleResourcePrivileges.forEach(e -> {
            String resourceKey = resourceManager.keyById(e.getResource_id());
            permissions.add(String.format("+%s+%d", resourceKey, e.getPrivilege()));
        });
        return permissions;
    }

    @Override
    public Map<String, Boolean> validateAuthWithUrl(List<AuthUrlParameterDomain> parameters) {
        Map<String, Boolean> results = new HashMap<>(parameters.size());
        //获取当前用户拥有的url pattern
        Set<String> urls = subjectUrls();
        parameters.stream().forEach(e -> {
            String auth_key = e.getAuth_key();
            results.put(auth_key, Boolean.FALSE);
            String auth_url = e.getAuth_url();
            //判断当前用户对URL是否有权限
            for (String url : urls) {
                if (pathMatcher.match(url, auth_url)) {
                    results.put(auth_key, Boolean.TRUE);
                    break;
                }
            }
        });
        return results;
    }

    @Override
    public Set<String> subjectUrls() {
        Map<String, Filter> filters = ShiroUtil.INSTANCE.getFilters();
        Subject subject = SecurityUtils.getSubject();
        //当前用户有权限的url pattern集合
        Set<String> urls = new HashSet<>();
        filters.forEach((filterName, filter) -> {
            if (!(filter instanceof StatelessFilter)) {
                return;
            }
            StatelessFilter statelessFilter = (StatelessFilter) filter;
            Map<String, Object> pathRoles = statelessFilter.appliedPaths();
            pathRoles.forEach((path, roles) -> {
                if ("justLogin".equals(filterName)) {
                    urls.add(path);
                    return;
                }
                if ("roles&permissions".equals(filterName)) {
                    String[] rolesArray = (String[]) roles;
                    for (String role : rolesArray) {
                        if (subject.hasRole(role)) {
                            urls.add(path);
                            break;
                        }
                    }
                }
            });
        });
        return urls;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void assign2Role(Long accountId, String roleKey) {
        Assert.INSTANCE.notNull(accountId, "账户不能为空");
        Assert.INSTANCE.notNull(roleKey, "角色不能为空");
        Long role_id = roleManager.idByKey(roleKey);
        // 判断角色账户是否存在
        boolean exist = roleAccountManager.exist(role_id, accountId);
        if (exist) {
            return;
        }
        // 判断账户是否存在
        exist = accountManager.exist(accountId);
        Assert.INSTANCE.isTrue(exist, "账户不存在");
        // 插入
        RoleAccountParameterDomain parameter = RoleAccountParameterDomain.newInstance();
        parameter.setRole_id(role_id);
        parameter.setAccount_id(accountId);
        parameter.setSummary("");
        roleAccountManager.insert(parameter);
    }
}
