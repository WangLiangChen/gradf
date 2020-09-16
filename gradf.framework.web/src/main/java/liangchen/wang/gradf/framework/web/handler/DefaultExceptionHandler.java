package liangchen.wang.gradf.framework.web.handler;

import liangchen.wang.gradf.framework.commons.enumeration.ExceptionCode;
import liangchen.wang.gradf.framework.commons.exception.GradfException;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.web.result.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangliangchen
 * <p>
 * 使用 @ ExceptionHandler 注解，进行异常处理的方法必须与出错的方法在同一个Controller里面
 * 实现 HandlerExceptionResolver 接口，官方不推荐使用
 * 使用 @ControllerAdvice+ @ ExceptionHandler 注解，不支持404，官方推荐
 * 使用ErrorController，获取不到异常的具体信息，适合根据状态码匹配，支持404
 */
@ControllerAdvice
public class DefaultExceptionHandler implements HandlerExceptionResolver {
    private final static Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public void resolveException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        if (!(e instanceof GradfException)) {
            ResponseUtil.createResponse(response).error(e).flush();
            return;
        }
        GradfException gradfException = GradfException.class.cast(e);
        String code = gradfException.getExceptionMessage().getCode();
        if (!ExceptionCode.NeedLogin.name().equals(code)) {
            ResponseUtil.createResponse(response).error(e).flush();
            return;
        }
        //获取请求中的需要登录处理策略的标识
        String needLoginStrategy = request.getHeader("NeedLoginStrategy");
        if (StringUtil.INSTANCE.isBlank(needLoginStrategy)) {
            needLoginStrategy = request.getParameter("NeedLoginStrategy");
        }
        //正常返回但是不带数据
        if ("ReturnEmpty".equals(needLoginStrategy)) {
            ResponseUtil.createResponse(response).flush();
            return;
        }
        ResponseUtil.createResponse(response).error(e).flush();
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        this.resolveException(request, response, e);
        return new ModelAndView();
    }

}
