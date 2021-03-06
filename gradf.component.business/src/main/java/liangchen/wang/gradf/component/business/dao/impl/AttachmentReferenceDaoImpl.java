package liangchen.wang.gradf.component.business.dao.impl;

import liangchen.wang.gradf.component.business.dao.IAttachmentReferenceDao;
import liangchen.wang.gradf.component.business.dao.entity.AttachmentReference;
import liangchen.wang.gradf.component.business.dao.query.AttachmentReferenceQuery;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LiangChen.Wang 2019-12-29 20:25:05
 */
@Repository("Gradf_Business_DefaultAttachmentReferenceDao")
public class AttachmentReferenceDaoImpl extends AbstractJdbcDao<AttachmentReference, AttachmentReferenceQuery> implements IAttachmentReferenceDao {

    @Override
    public void updateBatch(List<AttachmentReference> deleteEntities, List<AttachmentReference> insertEntities) {
        deleteEntities.forEach(entity -> {
            jdbcTemplate.update("delete from gradf_attachment_reference where attachment_id=? and business_id=? and business_type=? and attachment_flag=?",
                    entity.getAttachment_id(), entity.getBusiness_id(), entity.getBusiness_type(), entity.getAttachment_flag());
        });
        insertEntities.forEach(entity -> {
            this.insert(entity);
        });
    }
}
