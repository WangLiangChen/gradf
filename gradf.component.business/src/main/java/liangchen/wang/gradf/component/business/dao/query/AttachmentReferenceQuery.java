package liangchen.wang.gradf.component.business.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.query.RootQuery;
import liangchen.wang.gradf.framework.data.query.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2019-12-29 20:25:05
 */
@Table(name = "gradf_attachment_reference")
public class AttachmentReferenceQuery extends RootQuery {
    private static final AttachmentReferenceQuery self = new AttachmentReferenceQuery();

    public static AttachmentReferenceQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    @Query(operator = Operator.EQUALS, column = "attachment_id")
    private Long attachment_id;
    @Query(operator = Operator.EQUALS, column = "business_id")
    private Long business_id;
    @Query(operator = Operator.EQUALS, column = "business_type")
    private String business_type;
    @Query(operator = Operator.EQUALS, column = "attachment_flag")
    private String attachment_flag;


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

}
