package liangchen.wang.gradf.component.business.manager.impl;

import liangchen.wang.gradf.component.business.dao.IAttachmentReferenceDao;
import liangchen.wang.gradf.component.business.dao.entity.AttachmentReference;
import liangchen.wang.gradf.component.business.dao.query.AttachmentReferenceQuery;
import liangchen.wang.gradf.component.business.manager.IAttachmentReferenceManager;
import liangchen.wang.gradf.component.business.manager.domain.parameter.AttachmentReferenceParameterDomain;
import liangchen.wang.gradf.component.business.manager.domain.result.AttachmentReferenceResultDomain;
import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author LiangChen.Wang 2019-12-29 20:25:05
 */
@Component("Gradf_Business_DefaultAttachmentReferenceManager")
public class AttachmentReferenceManagerImpl extends AbstractManager<AttachmentReference, AttachmentReferenceQuery,AttachmentReferenceResultDomain> implements IAttachmentReferenceManager {
    @Inject
    public AttachmentReferenceManagerImpl(@Named("Gradf_Business_DefaultAttachmentReferenceDao") IAttachmentReferenceDao dao) {
        super("图片引用关系", "AttachmentReference", dao);
    }

    @Override
    public boolean insert(AttachmentReferenceParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter,  "参数不能为空");
        parameter.populateEntity((entity) -> {
            AttachmentReference attachmentReference = (AttachmentReference) entity;
            attachmentReference.initFields();
        });
        return super.insert(parameter);
    }

    @Override
    public boolean deleteByPrimaryKey(Long attachment_id, Long business_id, String business_type, String attachment_flag) {
        Assert.INSTANCE.notNull(attachment_id,  "参数不能为空");
        Assert.INSTANCE.notNull(business_id,  "参数不能为空");
        Assert.INSTANCE.notBlank(business_type,  "参数不能为空");
        Assert.INSTANCE.notBlank(attachment_flag,  "参数不能为空");
        AttachmentReferenceQuery query = AttachmentReferenceQuery.newInstance();
        query.setAttachment_id(attachment_id);
        query.setBusiness_id(business_id);
        query.setBusiness_type(business_type);
        query.setAttachment_flag(attachment_flag);
        int rows = super.deleteByQuery(query);
        return rows == 1;
    }

    @Override
    public int deleteByQuery(AttachmentReferenceQuery query) {
        Assert.INSTANCE.notNull(query,  "参数不能为空");
        return super.deleteByQuery(query);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Map<String, List<AttachmentReferenceParameterDomain>> handleAttachmentReference(List<AttachmentReferenceParameterDomain> attachments, Long business_id, String business_type, boolean isInsert) {
        if (CollectionUtil.INSTANCE.isEmpty(attachments)) {
            throw new InfoException("参数不能为空");
        }
        Assert.INSTANCE.notNull(business_id,  "参数不能为空");
        Assert.INSTANCE.notBlank(business_type,  "参数不能为空");
        attachments.forEach(e -> {
            e.setBusiness_type(business_type);
            e.setBusiness_id(business_id);
        });
        if (isInsert) {
            this.updateBatch(Collections.emptyList(), attachments);
            Map<String, List<AttachmentReferenceParameterDomain>> map = new HashMap<>(2);
            map.put("deleteDomains", Collections.emptyList());
            map.put("insertDomains", attachments);
            return map;
        }
        //更新 先查出已有关系
        List<AttachmentReferenceResultDomain> results = this.byBusiness(business_id, business_type);
        Map<String, AttachmentReferenceParameterDomain> resultMap = results.stream().collect(Collectors.toMap(e -> e.getAttachment_flag() + e.getAttachment_id(), e -> e.copyTo(AttachmentReferenceParameterDomain.class)));
        Map<String, AttachmentReferenceParameterDomain> parameterMap = attachments.stream().collect(Collectors.toMap(e -> e.getAttachment_flag() + e.getAttachment_id(), Function.identity()));
        Iterator<Map.Entry<String, AttachmentReferenceParameterDomain>> iterator = parameterMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, AttachmentReferenceParameterDomain> next = iterator.next();
            if (resultMap.containsKey(next.getKey())) {
                resultMap.remove(next.getKey());
                iterator.remove();
            }
        }
        List<AttachmentReferenceParameterDomain> deleteDomains = resultMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
        List<AttachmentReferenceParameterDomain> insertDomains = parameterMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
        this.updateBatch(deleteDomains, insertDomains);
        Map<String, List<AttachmentReferenceParameterDomain>> map = new HashMap<>(2);
        map.put("deleteDomains", deleteDomains);
        map.put("insertDomains", insertDomains);
        return map;
    }

    @Override
    public void updateBatch(List<AttachmentReferenceParameterDomain> deleteDomains, List<AttachmentReferenceParameterDomain> insertDomains) {
        Assert.INSTANCE.notNull(deleteDomains,  "参数不能为空");
        Assert.INSTANCE.notNull(insertDomains,  "参数不能为空");
        List<AttachmentReference> deleteEntities = new ArrayList<>(deleteDomains.size());
        deleteDomains.forEach(e -> {
            deleteEntities.add(e.copyTo(AttachmentReference.class));
        });
        List<AttachmentReference> insertEntities = new ArrayList<>(insertDomains.size());
        insertDomains.forEach(e -> {
            AttachmentReference entity = e.copyTo(AttachmentReference.class);
            entity.initFields();
            insertEntities.add(entity);
        });
        IAttachmentReferenceDao dao = this.getDao();
        dao.updateBatch(deleteEntities, insertEntities);
    }

    @Override
    public List<AttachmentReferenceResultDomain> byBusiness(Long business_id, String business_type) {
        Assert.INSTANCE.notNull(business_id,  "business_id不能为空");
        Assert.INSTANCE.notBlank(business_type,  "business_type不能为空");
        AttachmentReferenceQuery query = AttachmentReferenceQuery.newInstance();
        query.setBusiness_id(business_id);
        query.setBusiness_type(business_type);
        return super.list(query);
    }

    @Override
    public List<AttachmentReferenceResultDomain> byBusinessAndFlag(Long business_id, String business_type, String attachment_flag) {
        Assert.INSTANCE.notNull(business_id,  "business_id不能为空");
        Assert.INSTANCE.notBlank(business_type,  "business_type不能为空");
        Assert.INSTANCE.notBlank(attachment_flag,  "attachment_flag");
        AttachmentReferenceQuery query = AttachmentReferenceQuery.newInstance();
        query.setBusiness_id(business_id);
        query.setBusiness_type(business_type);
        query.setAttachment_flag(attachment_flag);
        return super.list(query);
    }

}
