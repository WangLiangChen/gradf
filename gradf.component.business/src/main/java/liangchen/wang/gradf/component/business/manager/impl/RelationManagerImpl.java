package liangchen.wang.gradf.component.business.manager.impl;

import liangchen.wang.gradf.component.business.dao.IRelationDao;
import liangchen.wang.gradf.component.business.dao.entity.Relation;
import liangchen.wang.gradf.component.business.dao.query.RelationQuery;
import liangchen.wang.gradf.component.business.manager.IRelationManager;
import liangchen.wang.gradf.component.business.manager.domain.parameter.RelationParameterDomain;
import liangchen.wang.gradf.component.business.manager.domain.result.RelationResultDomain;
import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.enumeration.DataMode;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.data.utils.UidDb;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author LiangChen.Wang 2020-04-19 22:39:49
 */
@Component("Gradf_Business_DefaultRelationManager")
public class RelationManagerImpl extends AbstractManager<Relation, RelationQuery, RelationResultDomain> implements IRelationManager {
    @Inject
    public RelationManagerImpl(@Named("Gradf_Business_DefaultRelationDao") IRelationDao dao) {
        super("关系", "Relation", dao);
    }

    @Override
    public boolean insert(RelationParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        final Long operator = FouraUtil.INSTANCE.getOperator();
        parameter.populateEntity((entity) -> {
            Relation relation = ClassBeanUtil.INSTANCE.cast(entity);
            //TODO 这里可以调整Entity，比如设置主键/状态等
            Assert.INSTANCE.notNullElseRun(relation.getRecord_id(), () -> relation.setRecord_id(UidDb.INSTANCE.uid()));
            Assert.INSTANCE.notNullElseRun(relation.getData_mode(), () -> relation.setData_mode(DataMode.A.getValue()));
            relation.setCreator(operator);
            relation.setModifier(operator);
            relation.initFields();
        });
        return super.insert(parameter);
    }

    @Override
    public boolean deleteByPrimaryKey(Long record_id) {
        RelationQuery query = RelationQuery.newInstance();
        query.setRecord_id(record_id);
        return super.deleteByQuery(query) == 1;
    }

    @Override
    public boolean updateByPrimaryKey(RelationParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        RelationQuery query = RelationQuery.newInstance();
        query.setRecord_id(parameter.getRecord_id());
        int rows = updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(RelationParameterDomain parameter, RelationQuery query) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        parameter.populateEntity((entity) -> {
            Relation relation = ClassBeanUtil.INSTANCE.cast(entity);
            // TODO 这里添加不更新或者不论是否空值总更新的字段
            relation.setModify_datetime(LocalDateTime.now());
            relation.setModifier(FouraUtil.INSTANCE.getOperator());
        });
        return super.updateByQuery(parameter, query);
    }

    @Override
    public RelationResultDomain byPrimaryKey(Long record_id) {
        Assert.INSTANCE.notNull(record_id, "参数不能为空");
        RelationQuery query = RelationQuery.newInstance();
        query.setRecord_id(record_id);
        return one(query);
    }

    @Override
    public RelationResultDomain byPrimaryKeyOrThrow(Long record_id) {
        Assert.INSTANCE.notNull(record_id, "参数不能为空");
        RelationQuery query = RelationQuery.newInstance();
        query.setRecord_id(record_id);
        RelationResultDomain result = byPrimaryKey(record_id);
        Assert.INSTANCE.notNull(result, "数据不存在");
        return result;
    }

    @Override
    public List<RelationResultDomain> list(RelationQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<RelationResultDomain> pagination(RelationQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

}
