package liangchen.wang.gradf.component.business.manager.impl;

import liangchen.wang.gradf.component.business.dao.IRegionDao;
import liangchen.wang.gradf.component.business.dao.entity.Region;
import liangchen.wang.gradf.component.business.dao.query.RegionQuery;
import liangchen.wang.gradf.component.business.manager.IRegionManager;
import liangchen.wang.gradf.component.business.manager.domain.parameter.RegionParameterDomain;
import liangchen.wang.gradf.component.business.manager.domain.result.RegionResultDomain;
import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.commons.validator.AssertLevel;
import liangchen.wang.gradf.framework.data.enumeration.Status;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author LiangChen.Wang 2019-12-29 20:30:26
 */
@Component("Gradf_Business_DefaultRegionManager")
public class RegionManagerImpl extends AbstractManager<Region, RegionQuery, RegionResultDomain> implements IRegionManager {
    @Inject
    public RegionManagerImpl(@Named("Gradf_Business_DefaultRegionDao") IRegionDao dao) {
        super("国标区域", "Region", dao);
    }

    @Override
    public boolean deleteByPrimaryKey(Long region_code) {
        Assert.INSTANCE.notNull(region_code, "区域代码不能为空");
        return updateStatusByPrimaryKey(region_code, Status.DELETED.name());
    }

    @Override
    public boolean updateByPrimaryKey(RegionParameterDomain parameter) {
        RegionQuery query = RegionQuery.newInstance();
        query.setRegion_code(parameter.getRegion_code());
        int rows = super.updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(RegionParameterDomain parameter, RegionQuery query) {
        return super.updateByQuery(parameter, query);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long region_code, String statusTo, String... statusFrom) {
        return updateStatusByPrimaryKey(region_code, statusTo, statusFrom, null);
    }

    @Override
    public boolean updateStatusByPrimaryKey(Long region_code, String statusTo, String[] statusIn, String[] statusNotIn) {
        RegionParameterDomain parameter = RegionParameterDomain.newInstance();
        parameter.setStatus(statusTo);
        RegionQuery query = RegionQuery.newInstance();
        query.setRegion_code(region_code);
        if (CollectionUtil.INSTANCE.isNotEmpty(statusIn)) {
            query.setStatusIn(statusIn);
        }
        if (CollectionUtil.INSTANCE.isNotEmpty(statusNotIn)) {
            query.setStatusNotIn(statusNotIn);
        }
        int rows = super.updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public RegionResultDomain byPrimaryKey(Long region_code, String... returnFields) {
        return byPrimaryKey(region_code, null, null, returnFields);
    }

    @Override
    public RegionResultDomain byPrimaryKeyOrThrow(Long region_code, String... returnFields) {
        return byPrimaryKeyOrThrow(region_code, null, null, returnFields);
    }

    @Override
    public RegionResultDomain byPrimaryKeyOrThrow(Long region_code, String[] statusIn, String[] statusNotIn, String... returnFields) {
        RegionResultDomain resultDomain = byPrimaryKey(region_code, statusIn, statusNotIn, returnFields);
        Assert.INSTANCE.notNull(resultDomain, "数据不存在或者状态错误");
        return resultDomain;
    }

    @Override
    public RegionResultDomain byPrimaryKey(Long region_code, String[] statusIn, String[] statusNotIn, String... returnFields) {
        RegionQuery query = RegionQuery.newInstance();
        query.setRegion_code(region_code);
        if (CollectionUtil.INSTANCE.isNotEmpty(statusIn)) {
            query.setStatusIn(statusIn);
        }
        if (CollectionUtil.INSTANCE.isNotEmpty(statusNotIn)) {
            query.setStatusNotIn(statusNotIn);
        }
        return super.one(query, returnFields);
    }

    @Override
    public List<RegionResultDomain> list(RegionQuery query, String... returnFields) {
        return super.list(query, (result) -> {
            RegionQuery countQuery = RegionQuery.newInstance();
            countQuery.setParent_code(result.getRegion_code());
            int count = super.count(countQuery);
            result.setChildrenNumber(count);
        }, returnFields);
    }

    @Override
    public PaginationResult<RegionResultDomain> pagination(RegionQuery query, String... returnFields) {
        return super.pagination(query, (result) -> {
            result.setStatus_text(Status.textByName(result.getStatus()));
        }, returnFields);
    }

}
