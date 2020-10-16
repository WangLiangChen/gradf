package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.component.foura.dao.IOperationLogDao;
import liangchen.wang.gradf.component.foura.dao.entity.OperationLog;
import liangchen.wang.gradf.component.foura.dao.query.OperationLogQuery;
import liangchen.wang.gradf.component.foura.manager.IOperationLogManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.OperationLogParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.OperationLogResultDomain;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.data.utils.UidDb;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author LiangChen.Wang 2020-03-04 14:58:01
 */
@Component("Gradf_Business_DefaultOperationLogManager")
public class OperationLogManagerImpl extends AbstractManager<OperationLog, OperationLogQuery, OperationLogResultDomain> implements IOperationLogManager {
    @Inject
    public OperationLogManagerImpl(@Named("Gradf_Business_DefaultOperationLogDao") IOperationLogDao dao) {
        super("操作日志", "OperationLog", dao);
    }

    @Override
    @Async
    public void insert(OperationLogParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        Assert.INSTANCE.notNullElseRun(parameter.getLog_id(), () -> parameter.setLog_id(UidDb.INSTANCE.uid()));
        Assert.INSTANCE.notBlankElseRun(parameter.getStatus(), () -> parameter.setStatus(Status.NORMAL.name()));
        parameter.populateEntity((operationLog) -> {
        });
        super.insert(parameter);
    }

    @Override
    public OperationLogResultDomain byPrimaryKey(Long log_id, String... returnFields) {
        return byPrimaryKey(log_id, null, null, returnFields);
    }

    @Override
    public OperationLogResultDomain byPrimaryKeyOrThrow(Long log_id, String... returnFields) {
        return byPrimaryKeyOrThrow(log_id, null, null, returnFields);
    }

    @Override
    public OperationLogResultDomain byPrimaryKeyOrThrow(Long log_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        OperationLogResultDomain resultDomain = byPrimaryKey(log_id, statusIn, statusNotIn, returnFields);
        Assert.INSTANCE.notNull(resultDomain, "数据不存在或者状态错误");
        return resultDomain;
    }

    @Override
    public OperationLogResultDomain byPrimaryKey(Long log_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        Assert.INSTANCE.notNull(log_id, "参数不能为空");
        OperationLogQuery query = OperationLogQuery.newInstance();
        query.setLog_id(log_id);
        if (CollectionUtil.INSTANCE.isNotEmpty(statusIn)) {
            query.setStatusIn(statusIn);
        }
        if (CollectionUtil.INSTANCE.isNotEmpty(statusNotIn)) {
            query.setStatusNotIn(statusNotIn);
        }
        return super.one(query, returnFields);
    }

    @Override
    public List<OperationLogResultDomain> list(OperationLogQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<OperationLogResultDomain> pagination(OperationLogQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

}
