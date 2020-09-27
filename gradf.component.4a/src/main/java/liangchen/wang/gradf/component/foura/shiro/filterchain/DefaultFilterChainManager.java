package liangchen.wang.gradf.component.foura.shiro.filterchain;

import liangchen.wang.gradf.component.foura.shiro.filter.LoginAuthorizationFilter;
import liangchen.wang.gradf.component.foura.shiro.filter.RolesAuthorizationFilter;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.SimpleNamedFilterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import java.util.List;

/**
 * @author LiangChen.Wang
 * 改造后支持多个chainName和URL的匹配
 */
public class DefaultFilterChainManager extends org.apache.shiro.web.filter.mgt.DefaultFilterChainManager {
    private static final Logger logger = LoggerFactory.getLogger(DefaultFilterChainManager.class);
	public FilterChain proxy(FilterChain original, List<String> chainNames) {
	    logger.debug("execute proxy,chainNames:{}",chainNames);
		NamedFilterList configured = new SimpleNamedFilterList(chainNames.toString());
		//遍历判断是否既包含login又包含roles
		boolean containLogin = false,containRoles = false;
		for (String chainName : chainNames) {
			NamedFilterList chain = getChain(chainName);
			for (Filter filter : chain) {
				configured.add(filter);
				if(filter instanceof LoginAuthorizationFilter){
					containLogin = true;
				}else if(filter instanceof RolesAuthorizationFilter){
					containRoles = true;
				}
			}
		}
		//如果都包含则移除Roles
		if(containLogin && containRoles){
			configured.removeIf(e->e instanceof RolesAuthorizationFilter);
		}
        FilterChain proxy = configured.proxy(original);
        return proxy;
	}

}
