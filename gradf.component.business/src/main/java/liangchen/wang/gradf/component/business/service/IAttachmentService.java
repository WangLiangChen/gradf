package liangchen.wang.gradf.component.business.service;

import liangchen.wang.gradf.component.business.manager.domain.result.AttachmentReferenceResultDomain;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @author LiangChen.Wang 2019/9/17 17:49
 */
public interface IAttachmentService {

    Long upload(MultipartFile multipartFile);

    Long upload(InputStream intputStream, String originalFilename, String contentType, Long size);

    String resolveUrl(Long attachment_id);

    List<AttachmentReferenceResultDomain> byBusiness(Long business_id, String business_type);

    List<AttachmentReferenceResultDomain> byBusinessAndFlag(Long business_id, String business_type, String attachment_flag);
}
