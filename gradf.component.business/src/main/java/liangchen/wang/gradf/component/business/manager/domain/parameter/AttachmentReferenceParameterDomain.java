package liangchen.wang.gradf.component.business.manager.domain.parameter;

import liangchen.wang.gradf.component.business.dao.entity.AttachmentReference;
import liangchen.wang.gradf.component.commons.base.ParameterDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author LiangChen.Wang 2019-12-29 20:25:05
*/
public class AttachmentReferenceParameterDomain extends ParameterDomain<AttachmentReference> {
    private static final AttachmentReferenceParameterDomain self = new AttachmentReferenceParameterDomain();

    public static AttachmentReferenceParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    @NotNull(message = "附件Id不能为为空")
    private Long attachment_id;
    @NotNull(message = "业务Id不能为为空")
    private Long business_id;
    @NotBlank(message = "业务类型不能为为空")
    private String business_type;
    @NotBlank(message = "附件标识不能为空")
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
