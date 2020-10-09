package liangchen.wang.gradf.component.business.manager.impl;

import liangchen.wang.gradf.component.business.dao.IAddressDao;
import liangchen.wang.gradf.component.business.dao.entity.Address;
import liangchen.wang.gradf.component.business.dao.query.AddressQuery;
import liangchen.wang.gradf.component.business.manager.IAddressManager;
import liangchen.wang.gradf.component.business.manager.IRelationManager;
import liangchen.wang.gradf.component.business.manager.domain.parameter.AddressParameterDomain;
import liangchen.wang.gradf.component.business.manager.domain.result.AddressResultDomain;
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
 * @author LiangChen.Wang 2020-03-24 08:35:30
 */
@Component("Gradf_Business_DefaultAddressManager")
public class AddressManagerImpl extends AbstractManager<Address, AddressQuery, AddressResultDomain> implements IAddressManager {
    private final IRelationManager relationManager;

    @Inject
    public AddressManagerImpl(@Named("Gradf_Business_DefaultAddressDao") IAddressDao dao,
                              @Named("Gradf_Business_DefaultRelationManager") IRelationManager relationManager) {
        super("地址", "Address", dao);
        this.relationManager = relationManager;
    }

    @Override
    public boolean insert(AddressParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        parameter.populateEntity((entity) -> {
            Address address = ClassBeanUtil.INSTANCE.cast(entity);
            Assert.INSTANCE.notNullElseRun(address.getAddress_id(), () -> address.setAddress_id(UidDb.INSTANCE.uid()));
            Assert.INSTANCE.notBlankElseRun(address.getStatus(), () -> address.setStatus(Status.NORMAL.name()));
        });
        return super.insert(parameter);
    }

    @Override
    public boolean deleteByPrimaryKey(Long address_id) {
        return updateStatusByPrimaryKey(address_id, Status.NORMAL.name());
    }

    @Override
    public boolean updateByPrimaryKey(AddressParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        AddressQuery query = AddressQuery.newInstance();
        query.setAddress_id(parameter.getAddress_id());
        int rows = updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(AddressParameterDomain parameter, AddressQuery query) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        parameter.populateEntity((entity) -> {
            Address address = ClassBeanUtil.INSTANCE.cast(entity);
            address.setModify_datetime(LocalDateTime.now());
            address.setModifier(ContextUtil.INSTANCE.getOperator());
        });
        return super.updateByQuery(parameter, query);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long address_id, String statusTo, String... statusFrom) {
        return updateStatusByPrimaryKey(address_id, statusTo, statusFrom, null);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long address_id, String statusTo, String[] statusIn, String[] statusNotIn) {
        Assert.INSTANCE.notNull(address_id, "参数不能为空");
        Assert.INSTANCE.notBlank(statusTo, "状态不能为空");
        AddressParameterDomain parameter = AddressParameterDomain.newInstance();
        parameter.setStatus(statusTo);
        AddressQuery query = AddressQuery.newInstance();
        query.setAddress_id(address_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        int rows = super.updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public AddressResultDomain byPrimaryKey(Long address_id, String... returnFields) {
        return byPrimaryKey(address_id, null, null, returnFields);
    }

    @Override
    public AddressResultDomain byPrimaryKeyOrThrow(Long address_id, String... returnFields) {
        return byPrimaryKeyOrThrow(address_id, null, null, returnFields);
    }

    @Override
    public AddressResultDomain byPrimaryKeyOrThrow(Long address_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        AddressResultDomain resultDomain = byPrimaryKey(address_id, statusIn, statusNotIn, returnFields);
        Assert.INSTANCE.notNull(resultDomain, "数据不存在或者状态错误");
        return resultDomain;
    }

    @Override
    public AddressResultDomain byPrimaryKey(Long address_id, String[] statusIn, String[] statusNotIn, String... returnFields) {
        Assert.INSTANCE.notNull(address_id, "参数不能为空");
        AddressQuery query = AddressQuery.newInstance();
        query.setAddress_id(address_id);
        query.setStatusIn(statusIn);
        query.setStatusNotIn(statusNotIn);
        return one(query, returnFields);
    }

    @Override
    public List<AddressResultDomain> list(AddressQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<AddressResultDomain> pagination(AddressQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }


}
