package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.commons.validator.AssertLevel;
import liangchen.wang.gradf.component.foura.dao.query.RoleQuery;
import liangchen.wang.gradf.component.foura.manager.IAccountManager;
import liangchen.wang.gradf.component.foura.manager.IAuthorizationManager;
import liangchen.wang.gradf.component.foura.manager.IRoleAccountManager;
import liangchen.wang.gradf.component.foura.manager.IRoleManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.AuthUrlParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.RoleAccountParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.RoleResultDomain;
import liangchen.wang.gradf.component.foura.shiro.filter.DefaultFilter;
import liangchen.wang.gradf.component.foura.shiro.utils.ShiroFilterChainUtil;
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
@Component("Crdf_Foura_DefaultAuthorizationManager")
public class AuthorizationManagerImpl implements IAuthorizationManager {
    private final PathMatcher pathMatcher = new AntPathMatcher();
    private final IRoleAccountManager roleAccountManager;
    private final IRoleManager roleManager;
    private final IAccountManager accountManager;

    @Inject
    public AuthorizationManagerImpl(@Named("Crdf_Foura_DefaultRoleAccountManager") IRoleAccountManager roleAccountManager,
                                    @Named("Crdf_Foura_DefaultRoleManager") IRoleManager roleManager,
                                    @Named("Crdf_Foura_DefaultAccountManager") IAccountManager accountManager) {
        this.roleAccountManager = roleAccountManager;
        this.roleManager = roleManager;
        this.accountManager = accountManager;
    }

    @Override
    public List<RoleResultDomain> rolesByAccountId(Long account_id, String... returnFields) {
        Assert.INSTANCE.notNull(account_id, AssertLevel.INFO, "账户ID不能为空");
        // 根据account_id查询角色
        Set<Long> roleIds = roleAccountManager.roleIdsByAccountId(account_id);
        RoleQuery query = RoleQuery.newInstance();
        query.setRoleIdIn(roleIds);
        return roleManager.list(query, returnFields);
    }

    @Override
    public Set<String> roleIdsByAccountId(Long account_id) {
        Assert.INSTANCE.notNull(account_id, AssertLevel.INFO, "账户ID不能为空");
        // 根据account_id查询角色
        Set<Long> roleIds = roleAccountManager.roleIdsByAccountId(account_id);
        return roleIds.stream().map(String::valueOf).collect(Collectors.toSet());
    }

    @Override
    public Set<String> permissionsByAccountId(Long account_id) {
        return Collections.emptySet();
    }

    @Override
    public Map<String, Boolean> validateAuthWithUrl(List<AuthUrlParameterDomain> parameters) {
        Map<String, Boolean> results = new HashMap<>(parameters.size());
        //获取当前用户拥有的url pattern
        Set<String> urls = subjectUrls();
        parameters.parallelStream().forEach(e -> {
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
        Map<String, Filter> filters = ShiroFilterChainUtil.getFilters();
        Subject subject = SecurityUtils.getSubject();
        //当前用户有权限的url pattern集合
        Set<String> urls = new HashSet<>();
        filters.forEach((filterName, filter) -> {
            if (!(filter instanceof DefaultFilter)) {
                return;
            }
            DefaultFilter defaultFilter = (DefaultFilter) filter;
            Map<String, Object> pathRoles = defaultFilter.appliedPaths();
            pathRoles.forEach((path, roles) -> {
                if ("login".equals(filterName)) {
                    urls.add(path);
                    return;
                }
                if ("roles".equals(filterName)) {
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
        Assert.INSTANCE.notNull(accountId, AssertLevel.INFO, "账户不能为空");
        Assert.INSTANCE.notNull(roleKey, AssertLevel.INFO, "角色不能为空");
        Long role_id = roleManager.idByKey(roleKey);
        // 判断角色账户是否存在
        boolean exist = roleAccountManager.exist(role_id, accountId);
        if (exist) {
            return;
        }
        // 判断账户是否存在
        exist = accountManager.exist(accountId);
        Assert.INSTANCE.isTrue(exist, AssertLevel.INFO, "账户不存在");
        // 插入
        RoleAccountParameterDomain parameter = RoleAccountParameterDomain.newInstance();
        parameter.setRole_id(role_id);
        parameter.setAccount_id(accountId);
        parameter.setSummary("");
        roleAccountManager.insert(parameter);
    }
}
