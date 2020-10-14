package liangchen.wang.gradf.component.foura.shiro.utils;

import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.Filter;
import java.util.Map;
import java.util.Set;

/**
 * @author LiangChen.Wang
 */
@Component
public class ShiroFilterChainUtil {
    private static final Logger logger = LoggerFactory.getLogger(ShiroFilterChainUtil.class);
    private static ShiroFilterChainUtil shiroFilterChainUtil;
    private final FilterChainManager filterChainManager;

    @Inject
    public ShiroFilterChainUtil(FilterChainManager filterChainManager) {
        this.filterChainManager = filterChainManager;
        shiroFilterChainUtil = this;
    }

    public static void addLoginToChain(String path) {
        logger.debug("append login filter chains,path:{}", path);
        shiroFilterChainUtil.filterChainManager.addToChain(path, "login");
    }

    public static void addRolesAndPermissionsToChain(String path, String roleOrPermissions) {
        logger.debug("append roles&permissions filter chains,path:{},roleOrPermissions:{}", path, roleOrPermissions);
        shiroFilterChainUtil.filterChainManager.addToChain(path, "roles&permissions", roleOrPermissions);
    }


    public static Map<String, Filter> getFilters() {
        return shiroFilterChainUtil.filterChainManager.getFilters();
    }

    public static Set<String> getChainNames() {
        return shiroFilterChainUtil.filterChainManager.getChainNames();
    }

    public static NamedFilterList getChain(String chainName) {
        return shiroFilterChainUtil.filterChainManager.getChain(chainName);
    }
}
