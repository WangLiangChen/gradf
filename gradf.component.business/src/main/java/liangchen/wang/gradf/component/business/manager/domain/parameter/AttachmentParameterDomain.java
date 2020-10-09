package liangchen.wang.gradf.component.business.manager.domain.parameter;

import liangchen.wang.gradf.component.business.dao.entity.Attachment;
import liangchen.wang.gradf.component.commons.base.ParameterDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author LiangChen.Wang 2020-02-11 09:14:24
 */
public class AttachmentParameterDomain extends ParameterDomain<Attachment> {
    private static final AttachmentParameterDomain self = new AttachmentParameterDomain();

    public static AttachmentParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    private Long attachment_id;
    @NotBlank(message = "附件名称不能为空")
    private String attachment_name;
    @NotNull(message = "附件大小不能为空")
    private Long attachment_size;
    @NotBlank(message = "附件类型不能为空")
    private String attachment_type;
    @NotBlank(message = "附件路径不能为空")
    private String attachment_path;
    private Long sort;
    private String summary;
    private String status;

    public Long getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(Long attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getAttachment_name() {
        return attachment_name;
    }

    public void setAttachment_name(String attachment_name) {
        this.attachment_name = attachment_name;
    }

    public Long getAttachment_size() {
        return attachment_size;
    }

    public void setAttachment_size(Long attachment_size) {
        this.attachment_size = attachment_size;
    }

    public String getAttachment_type() {
        return attachment_type;
    }

    public void setAttachment_type(String attachment_type) {
        this.attachment_type = attachment_type;
    }

    public String getAttachment_path() {
        return attachment_path;
    }

    public void setAttachment_path(String attachment_path) {
        this.attachment_path = attachment_path;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
