package liangchen.wang.gradf.component.business.dao.impl;

import liangchen.wang.gradf.component.business.dao.IAttachmentDao;
import liangchen.wang.gradf.component.business.dao.entity.Attachment;
import liangchen.wang.gradf.component.business.dao.query.AttachmentQuery;
import liangchen.wang.gradf.framework.cache.annotation.GradfAutoCacheable;
import liangchen.wang.gradf.framework.data.core.AbstractJdbcDao;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * @author LiangChen.Wang 2020-02-11 09:14:24
 */
@Repository("Gradf_Business_DefaultAttachmentDao")
@GradfAutoCacheable(clearMethods = {"insert", "delete*", "update*"}, durationRange = "1-24", timeUnit = TimeUnit.HOURS)
public class AttachmentDaoImpl extends AbstractJdbcDao<Attachment, AttachmentQuery> implements IAttachmentDao {

}
