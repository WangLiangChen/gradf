package liangchen.wang.gradf.component.business.manager.impl;

import liangchen.wang.gradf.component.business.dao.IInfiniteDao;
import liangchen.wang.gradf.component.business.dao.entity.Infinite;
import liangchen.wang.gradf.component.business.dao.query.InfiniteQuery;
import liangchen.wang.gradf.component.business.manager.IInfiniteManager;
import liangchen.wang.gradf.component.business.manager.domain.parameter.InfiniteParameterDomain;
import liangchen.wang.gradf.component.business.manager.domain.result.InfiniteResultDomain;
import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.framework.cluster.utils.LockUtil;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.utils.StringUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.commons.validator.AssertLevel;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.framework.data.utils.TransactionUtil;
import liangchen.wang.gradf.framework.data.utils.UidDb;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author LiangChen.Wang 2020-05-12 19:37:27
 * 因为要计算左右值，所以除查询外的所有操作必须加锁
 */
@Component("Gradf_Business_DefaultInfiniteManager")
public class InfiniteManagerImpl extends AbstractManager<Infinite, InfiniteQuery, InfiniteResultDomain> implements IInfiniteManager {
    private final String ROOT = "ROOT";

    @Inject
    public InfiniteManagerImpl(@Named("Gradf_Business_DefaultInfiniteDao") IInfiniteDao dao) {
        super("无限级分类", "Infinite", dao);
    }

    @Override
    public Long insertRoot(InfiniteParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        String business_flag = parameter.getBusiness_flag();
        Assert.INSTANCE.notBlank(business_flag, "业务标识business_flag不能为空");
        Assert.INSTANCE.notBlank(parameter.getInfinite_text(), "节点名称infinite_text不能为空");

        return LockUtil.INSTANCE.executeInLock(business_flag, () -> {
            // 判重,同一business_flag只能有一个ROOT
            InfiniteQuery query = InfiniteQuery.newInstance();
            query.setBusiness_flag(business_flag);
            query.setInfinite_flag(ROOT);
            int rows = count(query);
            Assert.INSTANCE.isTrue(rows == 0, AssertLevel.PROMPT, "节点已存在");

            parameter.populateEntity(entity -> {
                Infinite infinite = ClassBeanUtil.INSTANCE.cast(entity);
                populateInsertParameter(infinite);
                infinite.initFields();
                // 设置与Root节点相关的数据
                infinite.setParent_id(null);
                infinite.setInfinite_flag(ROOT);
                infinite.setInfinite_grade((byte) 0);
                infinite.setInfinite_left((short) 1);
                infinite.setInfinite_right((short) 2);
                parameter.setInfinite_id(infinite.getInfinite_id());
            });
            super.insert(parameter);
            return parameter.getInfinite_id();
        });
    }

    /**
     * 使用数据库锁要注意事务的隔离级别，在RR模式下，每个开启事务的线程，查询语句的查询结果都是跟其它线程隔离的
     * 也就是无法查询到其它线程已经提交的数据，因为在其它线程提交之前，此线程已经开启事务。
     */
    @Override
    public Long insertChild(InfiniteParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        Long parent_id = parameter.getParent_id();
        Assert.INSTANCE.notNull(parent_id, "父级节点不能为空");
        // 查询出business_flag用于锁定
        String business_flag = retrieveBusinessFlag(parent_id);
        return LockUtil.INSTANCE.executeInLock(business_flag, () -> {
            // 判断重复
            String infinite_flag = parameter.getInfinite_flag();
            if (StringUtil.INSTANCE.isNotBlank(infinite_flag)) {
                InfiniteQuery query = InfiniteQuery.newInstance();
                query.setBusiness_flag(business_flag);
                query.setInfinite_flag(infinite_flag);
                query.setStatusNotEquals(Status.DELETED.name());
                int count = count(query);
                Assert.INSTANCE.isTrue(count == 0, AssertLevel.PROMPT, "数据已存在");
            }
            Infinite infinite = parameter.copyTo(Infinite.class);
            populateInsertParameter(infinite);
            infinite.initFields();
            infinite.setBusiness_flag(business_flag);
            IInfiniteDao dao = this.getDao();
            // 开启事务执行
            TransactionUtil.INSTANCE.execute(() -> dao.insertChild(infinite));
            return infinite.getInfinite_id();
        });
    }

    @Override
    public void delete(Long infinite_id) {
        Assert.INSTANCE.notNull(infinite_id, "参数不能为空");
        // 查询出business_flag用于锁定
        String business_flag = retrieveBusinessFlag(infinite_id);
        LockUtil.INSTANCE.executeInLock(business_flag, () -> {
            IInfiniteDao dao = this.getDao();
            // 开启事务执行
            TransactionUtil.INSTANCE.execute(() -> dao.delete(infinite_id));
        });
    }

    private void populateInsertParameter(Infinite infinite) {
        final Long operator = FouraUtil.INSTANCE.getOperator();
        Assert.INSTANCE.notNullElseRun(infinite.getInfinite_id(), () -> infinite.setInfinite_id(UidDb.INSTANCE.uid()));
        Assert.INSTANCE.notBlankElseRun(infinite.getStatus(), () -> infinite.setStatus(Status.NORMAL.name()));
        infinite.setCreator(operator);
        infinite.setModifier(operator);
    }

    private String retrieveBusinessFlag(Long infinite_id) {
        Assert.INSTANCE.notNull(infinite_id, "参数infinite_id不能为空");
        InfiniteQuery query = InfiniteQuery.newInstance(infinite_id);
        InfiniteResultDomain resultDomain = one(query, "business_flag");
        Assert.INSTANCE.notNull(resultDomain, "数据不存在");
        return resultDomain.getBusiness_flag();
    }
}
