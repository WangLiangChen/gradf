package liangchen.wang.gradf.component.foura.shiro.filter;

import liangchen.wang.gradf.framework.commons.enumeration.ExceptionCode;
import liangchen.wang.gradf.framework.commons.exeception.ExceptionData;
import liangchen.wang.gradf.framework.commons.exeception.PromptException;
import liangchen.wang.gradf.framework.commons.json.JSON;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.component.foura.shiro.token.JwtToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LiangChen.Wang
 * 改造后支持Jwt Token
 */
public abstract class DefaultFilter extends AccessControlFilter {
    private static final Logger logger = LoggerFactory.getLogger(DefaultFilter.class);
    private static final String ACCESS_TOKEN = "access_token";

    /**
     * 获取配置的路径和角色集合
     *
     * @return
     */
    public Map<String, Object> appliedPaths() {
        return this.appliedPaths;
    }

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
        //return false;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = this.getSubject(request, response);
        logger.debug("mappedValue is:{},principal is:{}", JSON.toJSONString(mappedValue), subject.getPrincipal());
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String token = null;
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
        // 如果有token则认为是无状态请求,每个请求都得重新调用login以完成鉴权
        if (StringUtil.INSTANCE.isNotBlank(token)) {
            logger.debug("request header has '{}',the request is stateless", ACCESS_TOKEN);
            request.setAttribute(DefaultSubjectContext.SESSION_CREATION_ENABLED, Boolean.FALSE);
            JwtToken jwtToken = new JwtToken(token);
            try {
                //使用登录方法调用relam的方法获取认证和授权
                subject.login(jwtToken);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        if (CollectionUtil.INSTANCE.isEmpty(this.appliedPaths)) {
            return true;
        }
        List<String> configs = new ArrayList<>();
        this.appliedPaths.forEach((path, config) -> {
            if (pathsMatch(path, request) && null != config) {
                String[] objects = (String[]) config;
                for (String object : objects) {
                    configs.add(object);
                }
            }
        });
        String[] array = configs.toArray(new String[configs.size()]);
        return onPreHandle(request, response, array);
    }
}
