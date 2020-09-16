package liangchen.wang.gradf.framework.web.filter;


import liangchen.wang.gradf.framework.commons.exception.PromptException;
import liangchen.wang.gradf.framework.commons.utils.ContextUtil;
import liangchen.wang.gradf.framework.commons.utils.NetUtil;
import liangchen.wang.gradf.framework.web.result.ResponseUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * @author LiangChen.Wang
 */
public class RootFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 先清空一下
        ContextUtil.INSTANCE.remove();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 全局按线程保存IP
        String ip = NetUtil.INSTANCE.getIpFromRequest(request);
        ContextUtil.INSTANCE.putIp(ip);

        String requestURI = request.getRequestURI();
        if (requestURI.endsWith("favicon.ico")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ResponseWrapper responseWrapper = new ResponseWrapper(response);
        try {
            filterChain.doFilter(servletRequest, responseWrapper);
            int statusCode = responseWrapper.getStatusCode();
            if (SC_NOT_FOUND == statusCode) {
                throw new PromptException("访问的URL不存在:{}", requestURI);
            }
            if (0 == responseWrapper.getContentSize()) {
                ResponseUtil.createResponse(response).flush();
            } else {
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(responseWrapper.getContentAsByteArray());
                outputStream.flush();
            }
        } catch (Throwable throwable) {
            if (throwable instanceof ServletException) {
                throwable = throwable.getCause();
            }
            ResponseUtil.createResponse(response).error(throwable).flush();
        }
    }

    @Override
    public void destroy() {
        // 不管前面如何，最后一关清除
        ContextUtil.INSTANCE.clear();
        ContextUtil.INSTANCE.remove();
    }
}
