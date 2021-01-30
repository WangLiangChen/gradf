package liangchen.wang.gradf.component.commons.manager.impl;

import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.component.commons.dao.IDataOwnerDao;
import liangchen.wang.gradf.component.commons.dao.entity.DataOwner;
import liangchen.wang.gradf.component.commons.dao.query.DataOwnerQuery;
import liangchen.wang.gradf.component.commons.manager.IDataOwnerManager;
import liangchen.wang.gradf.component.commons.manager.domain.parameter.DataOwnerParameterDomain;
import liangchen.wang.gradf.component.commons.manager.domain.result.DataOwnerResultDomain;
import liangchen.wang.gradf.framework.api.ILock;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.data.utils.DbLockUtil;
import liangchen.wang.gradf.framework.data.utils.UidDb;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author LiangChen.Wang 2019-12-29 20:29:37
*/
@Component("Gradf_Business_DefaultDataOwnerManager")
public class DataOwnerManagerImpl extends AbstractManager<DataOwner, DataOwnerQuery, DataOwnerResultDomain> implements IDataOwnerManager {
    @Inject
    public DataOwnerManagerImpl(@Named("Gradf_Business_DefaultDataOwnerDao") IDataOwnerDao dao) {
        super("数据属主", "DataOwner", dao);
    }

    @Override
    public boolean insert(DataOwnerParameterDomain parameter) {
        ILock lock = DbLockUtil.INSTANCE.obtainLock();
        try{
            lock.lock("dataowner");

        }finally {
            lock.unlock("dataowner");
        }
        parameter.populateEntity((entity) -> {
            DataOwner dataOwner = (DataOwner) entity;
            dataOwner.setRecord_id(UidDb.INSTANCE.uid());
        });
        return super.insert(parameter);
    }

    @Override
    public boolean deleteByPrimaryKey(Long record_id) {
        DataOwnerQuery query = DataOwnerQuery.newInstance();
        query.setRecord_id(record_id);
        int rows = super.deleteByQuery(query);
        return rows == 1;
    }

    @Override
    public int deleteByQuery(DataOwnerQuery query) {
        return super.deleteByQuery(query);
    }

    @Override
    public boolean updateByPrimaryKey(DataOwnerParameterDomain parameter) {
        DataOwnerQuery query = DataOwnerQuery.newInstance();
        query.setRecord_id(parameter.getRecord_id());
        int rows = super.updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(DataOwnerParameterDomain parameter, DataOwnerQuery query) {
        return super.updateByQuery(parameter, query);
    }

    @Override
    public DataOwnerResultDomain byPrimaryKey(Long record_id) {
        DataOwnerQuery query = DataOwnerQuery.newInstance();
        query.setRecord_id(record_id);
        List<DataOwnerResultDomain> list = super.list(query);
        if(CollectionUtil.INSTANCE.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    @Override
    public DataOwnerResultDomain byPrimaryKeyOrThrow(Long record_id) {
        DataOwnerResultDomain resultDomain = byPrimaryKey(record_id);
        Assert.INSTANCE.isNull(resultDomain, "数据不存在");
        return resultDomain;
    }

    @Override
    public List<DataOwnerResultDomain> list(DataOwnerQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<DataOwnerResultDomain> pagination(DataOwnerQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

}
