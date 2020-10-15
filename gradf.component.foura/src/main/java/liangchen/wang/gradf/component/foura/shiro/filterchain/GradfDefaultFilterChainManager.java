package liangchen.wang.gradf.component.foura.shiro.filterchain;

import liangchen.wang.gradf.component.foura.shiro.filter.LoginAuthorizationFilter;
import liangchen.wang.gradf.component.foura.shiro.filter.RolesAndPermissionsAuthorizationFilter;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.SimpleNamedFilterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import java.util.Set;

/**
 * 改造以支持多个chainName和URL的匹配
 *
 * @author LiangChen.Wang
 */
public class GradfDefaultFilterChainManager extends DefaultFilterChainManager {
    private static final Logger logger = LoggerFactory.getLogger(GradfDefaultFilterChainManager.class);

    /**
     * 重载方法以处理多个chainName
     *
     * @param original
     * @param chainNames
     * @return
     */
    public FilterChain proxy(FilterChain original, Set<String> chainNames) {
        logger.debug("execute proxy,chainNames:{}", JsonUtil.INSTANCE.toJsonString(chainNames));
        NamedFilterList configured = new SimpleNamedFilterList("chainNames");
        //遍历判断是否既包含login又包含roles
        boolean containLoginFilter = false, containRolesAndPermissionsFilter = false;
        for (String chainName : chainNames) {
            NamedFilterList chain = getChain(chainName);
            for (Filter filter : chain) {
                configured.add(filter);
                if (filter instanceof LoginAuthorizationFilter) {
                    containLoginFilter = true;
                } else if (filter instanceof RolesAndPermissionsAuthorizationFilter) {
                    containRolesAndPermissionsFilter = true;
                }
            }
        }
        //如果都包含则移除Roles
        if (containLoginFilter && containRolesAndPermissionsFilter) {
            configured.removeIf(e -> e instanceof RolesAndPermissionsAuthorizationFilter);
        }
        return configured.proxy(original);
    }

}
