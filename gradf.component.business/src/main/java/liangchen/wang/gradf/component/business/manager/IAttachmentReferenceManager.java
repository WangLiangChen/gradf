package liangchen.wang.gradf.component.business.manager;

import liangchen.wang.gradf.component.business.dao.query.AttachmentReferenceQuery;
import liangchen.wang.gradf.component.business.manager.domain.parameter.AttachmentReferenceParameterDomain;
import liangchen.wang.gradf.component.business.manager.domain.result.AttachmentReferenceResultDomain;

import java.util.List;
import java.util.Map;

/**
 * @author LiangChen.Wang 2019-12-29 20:25:05
 */
public interface IAttachmentReferenceManager  {

    boolean insert(AttachmentReferenceParameterDomain parameter);

    boolean deleteByPrimaryKey(Long attachment_id, Long business_id, String business_type, String attachment_flag);

    int deleteByQuery(AttachmentReferenceQuery query);

    /**
     * @param attachments
     * @param business_id
     * @param business_type
     * @param isInsert
     * @return 删除的集合和插入的集合，key分别为deleteDomains和insertDomains
     */
    Map<String, List<AttachmentReferenceParameterDomain>> handleAttachmentReference(List<AttachmentReferenceParameterDomain> attachments, Long business_id, String business_type, boolean isInsert);

    void updateBatch(List<AttachmentReferenceParameterDomain> deleteDomains, List<AttachmentReferenceParameterDomain> insertDomains);

    List<AttachmentReferenceResultDomain> byBusiness(Long business_id, String business_type);

    List<AttachmentReferenceResultDomain> byBusinessAndFlag(Long business_id, String business_type, String attachment_flag);
}
