package liangchen.wang.gradf.component.business.manager.impl;

import liangchen.wang.gradf.component.business.dao.IAttachmentDao;
import liangchen.wang.gradf.component.business.dao.entity.Attachment;
import liangchen.wang.gradf.component.business.dao.query.AttachmentQuery;
import liangchen.wang.gradf.component.business.manager.IAttachmentManager;
import liangchen.wang.gradf.component.business.manager.domain.parameter.AttachmentParameterDomain;
import liangchen.wang.gradf.component.business.manager.domain.result.AttachmentResultDomain;
import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.utils.ContextUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.data.utils.UidDb;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LiangChen.Wang 2020-02-11 09:14:24
 */
@Component("Gradf_Business_DefaultAttachmentManager")
public class AttachmentManagerImpl extends AbstractManager<Attachment, AttachmentQuery, AttachmentResultDomain> implements IAttachmentManager {
    @Inject
    public AttachmentManagerImpl(@Named("Gradf_Business_DefaultAttachmentDao") IAttachmentDao dao) {
        super("附件", "Attachment", dao);
    }

    @Override
    public boolean insert(AttachmentParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "附件参数不能为空");
        final Long operator = ContextUtil.INSTANCE.getOperator();
        parameter.populateEntity((entity) -> {
            Attachment attachment = ClassBeanUtil.INSTANCE.cast(entity);
            Assert.INSTANCE.notNullElseRun(attachment.getAttachment_id(), () -> attachment.setAttachment_id(UidDb.INSTANCE.uid()));
            Assert.INSTANCE.notBlankElseRun(attachment.getStatus(), () -> attachment.setStatus(Status.NORMAL.name()));
            attachment.setCreator(operator);
            attachment.setModifier(operator);
        });
        return super.insert(parameter);
    }

    @Override
    public boolean deleteByPrimaryKey(Long attachment_id) {
        return updateStatusByPrimaryKey(attachment_id, Status.DELETED.name());
    }

    @Override
    public boolean updateByPrimaryKey(AttachmentParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        AttachmentQuery query = AttachmentQuery.newInstance();
        query.setAttachment_id(parameter.getAttachment_id());
        int rows = updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(AttachmentParameterDomain parameter, AttachmentQuery query) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        parameter.populateEntity((entity) -> {
            Attachment attachment = ClassBeanUtil.INSTANCE.cast(entity);
            attachment.setModify_datetime(LocalDateTime.now());
            attachment.setModifier(ContextUtil.INSTANCE.getOperator());
        });
        return super.updateByQuery(parameter, query);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long attachment_id, String statusTo, String... statusFrom) {
        return updateStatusByPrimaryKey(attachment_id, statusTo, statusFrom, null);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long attachment_id, String statusTo, String[] statusIn, String[] statusNotIn) {
        Assert.INSTANCE.notNull(attachment_id, "参数不能为空");
        Assert.INSTANCE.notBlank(statusTo, "状态不能为空");
        AttachmentParameterDomain parameter = AttachmentParameterDomain.newInstance();
        parameter.setStatus(statusTo);
        AttachmentQuery query = AttachmentQuery.newInstance();
        query.setAttachment_id(attachment_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        int rows = super.updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public AttachmentResultDomain byPrimaryKey(Long attachment_id, String... returnFields) {
        return byPrimaryKey(attachment_id, null, returnFields);
    }

    @Override
    public AttachmentResultDomain byPrimaryKeyOrThrow(Long attachment_id, String... returnFields) {
        return byPrimaryKeyOrThrow(attachment_id, null, returnFields);
    }

    @Override
    public AttachmentResultDomain byPrimaryKeyOrThrow(Long attachment_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        AttachmentResultDomain resultDomain = byPrimaryKey(attachment_id, statusIn, statusNotIn, returnFields);
        Assert.INSTANCE.notNull(resultDomain, "数据不存在或者状态错误");
        return resultDomain;
    }

    @Override
    public AttachmentResultDomain byPrimaryKey(Long attachment_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        Assert.INSTANCE.notNull(attachment_id, "参数不能为空");
        AttachmentQuery query = AttachmentQuery.newInstance();
        query.setAttachment_id(attachment_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        return super.one(query, returnFields);
    }

    @Override
    public List<AttachmentResultDomain> list(AttachmentQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<AttachmentResultDomain> pagination(AttachmentQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

}
