package liangchen.wang.gradf.component.business.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author LiangChen.Wang 2020-02-11 09:14:24
*/
@Entity(name = "gradf_attachment")
public class Attachment extends BaseEntity {
    private static final Attachment self = new Attachment();

    public static Attachment newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    @Id
    private Long attachment_id;
    private String attachment_name;
    private Long attachment_size;
    private String attachment_type;
    private String attachment_path;
    private Integer download_number;
    private String attachment_md5;
    private String attachment_sha1;
    private String attachment_crc32;

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

    public Integer getDownload_number() {
        return download_number;
    }

    public void setDownload_number(Integer download_number) {
        this.download_number = download_number;
    }

    public String getAttachment_md5() {
        return attachment_md5;
    }

    public void setAttachment_md5(String attachment_md5) {
        this.attachment_md5 = attachment_md5;
    }

    public String getAttachment_sha1() {
        return attachment_sha1;
    }

    public void setAttachment_sha1(String attachment_sha1) {
        this.attachment_sha1 = attachment_sha1;
    }

    public String getAttachment_crc32() {
        return attachment_crc32;
    }

    public void setAttachment_crc32(String attachment_crc32) {
        this.attachment_crc32 = attachment_crc32;
    }

}
