package liangchen.wang.gradf.component.business.controller;


import liangchen.wang.gradf.component.business.manager.domain.result.AttachmentReferenceResultDomain;
import liangchen.wang.gradf.component.business.service.IAttachmentService;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.web.result.ResponseUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author LiangChen.Wang 2019/9/16 9:22
 */
public abstract class AbstractAttachmentController {
    @Inject
    private IAttachmentService service;

    protected void upload(HttpServletRequest request, HttpServletResponse response) {
        if (!(request instanceof MultipartHttpServletRequest)) {
            throw new InfoException("请求中没有附件");
        }
        String fileField = request.getParameter("fileField");
        if (StringUtil.INSTANCE.isBlank(fileField)) {
            fileField = "file";
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile(fileField);
        Long attachment_id = service.upload(multipartFile);
        ResponseUtil.createResponse().data(attachment_id).flush();
    }

    protected void resolveUrl(Long attachment_id) {
        String url = service.resolveUrl(attachment_id);
        ResponseUtil.createResponse().data(url).flush();
    }

    protected void image(Long attachment_id, HttpServletResponse response) {
        String url = service.resolveUrl(attachment_id);
        response.setHeader("Location", url);
        response.setStatus(302);
    }

    protected void byBusiness(Long business_id, String business_type) {
        List<AttachmentReferenceResultDomain> resultDomains = service.byBusiness(business_id, business_type);
        ResponseUtil.createResponse().data(resultDomains).flush();
    }

    protected void byBusinessAndFlag(Long business_id, String business_type, String attachment_flag) {
        List<AttachmentReferenceResultDomain> resultDomains = service.byBusinessAndFlag(business_id, business_type, attachment_flag);
        ResponseUtil.createResponse().data(resultDomains).flush();
    }

}
