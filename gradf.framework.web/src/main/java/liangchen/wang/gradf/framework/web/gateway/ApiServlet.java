package liangchen.wang.gradf.framework.web.gateway;


import liangchen.wang.gradf.framework.commons.exception.ErrorException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author LiangChen.Wang
 */
//url pattern:/business/gateway/{apiName}
public class ApiServlet extends HttpServlet {
    public ApiServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ApiHandler.INSTANCE.handle(req, resp);
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }
}
