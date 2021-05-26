package liangchen.wang.gradf.component.foura.shiro.utils;

import liangchen.wang.gradf.framework.springboot.context.BeanLoader;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import java.util.Map;
import java.util.Set;

/**
 * @author LiangChen.Wang
 */
public enum ShiroUtil {
    // instance
    INSTANCE;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final FilterChainManager filterChainManager = BeanLoader.INSTANCE.getBean(FilterChainManager.class);

    public void addJustLoginToChain(String path) {
        logger.debug("append login filter chains,path:{}", path);
        this.filterChainManager.addToChain(path, "justLogin");
    }

    public void addRolesAndPermissionsToChain(String path, String roleOrPermissions) {
        logger.debug("append roles&permissions filter chains,path:{},roleOrPermissions:{}", path, roleOrPermissions);
        this.filterChainManager.addToChain(path, "roles&permissions", roleOrPermissions);
    }


    public Map<String, Filter> getFilters() {
        return this.filterChainManager.getFilters();
    }

    public Set<String> getChainNames() {
        return this.filterChainManager.getChainNames();
    }

    public NamedFilterList getChain(String chainName) {
        return this.filterChainManager.getChain(chainName);
    }
}
