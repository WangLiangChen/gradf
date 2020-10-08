package liangchen.wang.gradf.component.foura.shiro.filterchain;

import liangchen.wang.gradf.framework.commons.exception.PromptException;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashSet;
import java.util.Set;

/**
 * 改造后，可根据当前URL获取匹配的多个chainName
 * 原来的方法仅仅匹配第一个
 *
 * @author LiangChen.Wang
 */
public class GradfPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {
    private static final Logger logger = LoggerFactory.getLogger(GradfPathMatchingFilterChainResolver.class);

    /**
     * 覆盖方法 以处理url匹配多个chainName
     *
     * @param request
     * @param response
     * @param originalChain
     * @return
     */
    @Override
    public FilterChain getChain(ServletRequest request, ServletResponse response, FilterChain originalChain) {
        GradfDefaultFilterChainManager filterChainManager = (GradfDefaultFilterChainManager) getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            throw new PromptException("FilterChain未配置,无权访问");
        }
        // 通过方法addToChain(String chainName, String filterName, String chainSpecificFilterConfig)
        // 将filter加入到chain，chainName即url的AntPath表达式
        Set<String> configedChainNames = filterChainManager.getChainNames();
        // 1、获取当前请求的URL（不带上下文）
        String requestURI = getPathWithinApplication(request);
        logger.debug("当前请求的URL：{}", requestURI);
        logger.debug("已配置的ChainNames:{}", JsonUtil.INSTANCE.toJsonString(configedChainNames));
        Set<String> chainNames = new HashSet<>();
        for (String pathPattern : configedChainNames) {
            if (pathMatches(pathPattern, requestURI)) {
                chainNames.add(pathPattern);
            }
        }
        if (chainNames.size() == 0) {
            throw new PromptException("URL:{}无匹配,无权访问", requestURI);
            //return originalChain;
        } else {
            logger.debug("URL:{} 匹配的ChainNames:{}", requestURI, JsonUtil.INSTANCE.toJsonString(chainNames));
        }
        //默认的实现有问题，如果多个拦截器链都匹配了url，只返回第一个找到的拦截器链,改造后，将多个匹配的拦截器链合并返回
        return filterChainManager.proxy(originalChain, chainNames);
    }
}
