package liangchen.wang.gradf.component.business.dao;

import liangchen.wang.gradf.component.business.dao.entity.AttachmentReference;
import liangchen.wang.gradf.component.business.dao.query.AttachmentReferenceQuery;
import liangchen.wang.gradf.framework.data.core.IDao;

import java.util.List;

/**
 * @author LiangChen.Wang 2019-12-29 20:25:05
 */
public interface IAttachmentReferenceDao extends IDao<AttachmentReference, AttachmentReferenceQuery> {

    void updateBatch(List<AttachmentReference> deleteEntities, List<AttachmentReference> insertEntities);
}
