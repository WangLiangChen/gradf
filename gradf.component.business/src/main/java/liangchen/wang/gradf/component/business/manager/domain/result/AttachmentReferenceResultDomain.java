package liangchen.wang.gradf.component.business.manager.domain.result;

import liangchen.wang.gradf.component.commons.base.ResultDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author LiangChen.Wang 2019-12-29 20:25:05
*/
public class AttachmentReferenceResultDomain extends ResultDomain {
    private static final AttachmentReferenceResultDomain self = new AttachmentReferenceResultDomain();

    public static AttachmentReferenceResultDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    private Long attachment_id;
    private Long business_id;
    private String business_type;
    private String attachment_flag;
    private String attachment_url;
    private String summary;

    public Long getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(Long attachment_id) {
        this.attachment_id = attachment_id;
    }

    public Long getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Long business_id) {
        this.business_id = business_id;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getAttachment_flag() {
        return attachment_flag;
    }

    public void setAttachment_flag(String attachment_flag) {
        this.attachment_flag = attachment_flag;
    }

    public String getAttachment_url() {
        return attachment_url;
    }

    public void setAttachment_url(String attachment_url) {
        this.attachment_url = attachment_url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
