package liangchen.wang.gradf.component.foura.shiro.filter;

import liangchen.wang.gradf.component.foura.shiro.token.JwtToken;
import liangchen.wang.gradf.framework.commons.enumeration.ExceptionCode;
import liangchen.wang.gradf.framework.commons.exception.ExceptionData;
import liangchen.wang.gradf.framework.commons.exception.PromptException;
import liangchen.wang.gradf.framework.commons.json.JsonUtil;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.assertj.core.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 改造以支持JwtToken
 *
 * @author LiangChen.Wang
 */
public abstract class GradfFilter extends AccessControlFilter {
    private static final Logger logger = LoggerFactory.getLogger(GradfFilter.class);
    private static final String ACCESS_TOKEN = "access_token";

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        Subject subject = getSubject(request, response);
        logger.debug("subject's principal is:{}", subject.getPrincipal());

        if (null == subject.getPrincipal()) {
            throw new PromptException(ExceptionData.newInstance(ExceptionCode.NeedLogin.name()), "需要登录");
        } else {
            throw new PromptException(ExceptionData.newInstance(ExceptionCode.NeedLogin.name()), "没有权限");
            // WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    /**
     * 获取配置的路径和角色集合
     *
     * @return
     */
    public Map<String, Object> appliedPaths() {
        return this.appliedPaths;
    }

    /**
     * 合并路径对应的角色为新数组
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        if (CollectionUtil.INSTANCE.isEmpty(this.appliedPaths)) {
            return true;
        }
        Set<String> configs = new HashSet<>();
        this.appliedPaths.forEach((path, config) -> {
            if (pathsMatch(path, request) && Arrays.isArray(config)) {
                String[] objects = (String[]) config;
                for (String object : objects) {
                    configs.add(object);
                }
            }
        });
        String[] array = configs.toArray(new String[configs.size()]);
        return onPreHandle(request, response, array);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = this.getSubject(request, response);
        logger.debug("mappedValue is:{},principal is:{}", JsonUtil.INSTANCE.toJsonString(mappedValue), subject.getPrincipal());
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String token;
        //先查询Header
        token = httpRequest.getHeader(ACCESS_TOKEN);
        //再查询cookie
        if (StringUtil.INSTANCE.isBlank(token)) {
            Cookie[] cookies = httpRequest.getCookies();
            if (CollectionUtil.INSTANCE.isNotEmpty(cookies)) {
                for (Cookie cookie : cookies) {
                    if (ACCESS_TOKEN.equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        //最后查询参数
        if (StringUtil.INSTANCE.isBlank(token)) {
            token = httpRequest.getParameter(ACCESS_TOKEN);
        }
        // 如果有token则认为是无状态请求,每个请求都得重新调用login以完成认证
        if (StringUtil.INSTANCE.isNotBlank(token)) {
            logger.debug("request has '{}',the request is stateless", ACCESS_TOKEN);
            request.setAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED, Boolean.FALSE);
            JwtToken jwtToken = new JwtToken(token);
            // 使用登录方法调用relam的方法获取认证和授权
            subject.login(jwtToken);
        }
        return true;
    }

}
