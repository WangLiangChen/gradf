package liangchen.wang.gradf.component.business.dao.impl;

import liangchen.wang.gradf.component.business.dao.IAttachmentReferenceDao;
import liangchen.wang.gradf.component.business.dao.entity.AttachmentReference;
import liangchen.wang.gradf.component.business.dao.query.AttachmentReferenceQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.base.AbstractBaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2019-12-29 20:25:05
 */
@Repository("Gradf_Business_DefaultAttachmentReferenceDao")
@GradfAutoCacheable(clearMethods = {"insert", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class AttachmentReferenceDaoImpl extends AbstractBaseDao<AttachmentReference, AttachmentReferenceQuery> implements IAttachmentReferenceDao {

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
