package liangchen.wang.gradf.framework.web.controller;

import liangchen.wang.gradf.framework.commons.utils.DateTimeUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.web.result.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LiangChen.Wang 2019/9/28 15:06
 */
@RestController
@RequestMapping("/")
public class GlobalController {
    @GetMapping("/alive")
    public void alive() {
        ResponseUtil.createResponse().flushString("success");
    }

    @GetMapping("/basePath")
    public void basePath(HttpServletRequest request) {
        StringBuilder basePath = new StringBuilder();
        basePath.append(request.getScheme())
                .append("://")
                .append(request.getServerName())
                .append(":")
                .append(request.getServerPort());
        String contextPath = request.getContextPath();
        if (StringUtil.INSTANCE.isNotBlank(contextPath)) {
            basePath.append(contextPath)
                    .append("/");
        }
        basePath.append(request.getServletPath())
                .append("/");
        ResponseUtil.createResponse().flushString(basePath.toString());
    }

    @GetMapping("/datetime")
    public void datetime() {
        String yyyy_mm_dd_hh_mm_ss = DateTimeUtil.INSTANCE.getYYYY_MM_DD_HH_MM_SS();
        ResponseUtil.createResponse().flushString(yyyy_mm_dd_hh_mm_ss);
    }

    @GetMapping("/date")
    public void date() {
        String yyyy_mm_dd = DateTimeUtil.INSTANCE.getYYYY_MM_DD();
        ResponseUtil.createResponse().flushString(yyyy_mm_dd);
    }

    @GetMapping("/millisecond")
    public void millisecond() {
        ResponseUtil.createResponse().flushString(Long.toString(System.currentTimeMillis()));
    }
}
