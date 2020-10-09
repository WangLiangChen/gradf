package liangchen.wang.gradf.component.business.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.base.RootEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author LiangChen.Wang 2019-12-29 20:25:05
*/
@Entity(name = "gradf_attachment_reference")
public class AttachmentReference extends RootEntity {
    private static final AttachmentReference self = new AttachmentReference();

    public static AttachmentReference newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    @Id
    private Long attachment_id;
    @Id
    private Long business_id;
    @Id
    private String business_type;
    @Id
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
