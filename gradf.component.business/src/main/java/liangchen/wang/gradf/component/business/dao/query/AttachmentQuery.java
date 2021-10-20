package liangchen.wang.gradf.component.business.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.query.RootQuery;
import liangchen.wang.gradf.framework.data.query.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-02-11 09:14:24
*/
@Table(name = "gradf_attachment")
public class AttachmentQuery extends RootQuery {
    private static final AttachmentQuery self = new AttachmentQuery();

    public static AttachmentQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static AttachmentQuery newInstance(Long attachment_id) {
        AttachmentQuery query = newInstance();
        query.setAttachment_id(attachment_id);
        return query;
    }

    @Query(operator = Operator.EQUALS, column = "attachment_id")
    private Long attachment_id;

    @Query(operator = Operator.EQUALS, column = "status")
    private String status;
    @Query(operator = Operator.IN, column = "status")
    private String[] statusIn;
    @Query(operator = Operator.NOTIN, column = "status")
    private String[] statusNotIn;

    public Long getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(Long attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getStatusIn() {
        return statusIn;
    }

    public void setStatusIn(String[] statusIn) {
        this.statusIn = statusIn;
    }

    public String[] getStatusNotIn() {
        return statusNotIn;
    }

    public void setStatusNotIn(String[] statusNotIn) {
        this.statusNotIn = statusNotIn;
    }
}
