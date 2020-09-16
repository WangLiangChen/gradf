package liangchen.wang.gradf.framework.web.controller;

import liangchen.wang.gradf.framework.web.result.ResponseUtil;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author LiangChen.Wang
 */
@Controller
@RequestMapping("/")
public class DefaultErrorController extends AbstractErrorController {

    public DefaultErrorController() {
        super(new DefaultErrorAttributes());
    }

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public void error(HttpServletRequest request, HttpServletResponse response) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        ResponseUtil.createResponse(response).data(throwable).flush();
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

}
