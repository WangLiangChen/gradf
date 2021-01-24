package liangchen.wang.gradf.component.foura.manager.impl;


import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.component.foura.dao.IResourceDao;
import liangchen.wang.gradf.component.foura.dao.entity.Resource;
import liangchen.wang.gradf.component.foura.dao.query.ResourceQuery;
import liangchen.wang.gradf.component.foura.manager.IResourceManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.ResourceParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.ResourceResultDomain;
import liangchen.wang.gradf.framework.cluster.utils.LockUtil;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.commons.validator.AssertLevel;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.data.utils.UidDb;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;


/**
 * @author LiangChen.Wang 2019-12-25 23:17:21
 */
@Component("Gradf_Foura_DefaultResourceManager")
public class ResourceManagerImpl extends AbstractManager<Resource, ResourceQuery, ResourceResultDomain> implements IResourceManager {
    @Inject
    public ResourceManagerImpl(@Named("Gradf_Foura_DefaultResourceDao") IResourceDao dao) {
        super("资源", "Resource", dao);
    }

    @Override
    public boolean insert(ResourceParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        String resource_key = parameter.getResource_key();
        Assert.INSTANCE.notBlank(resource_key, "resource_key不能为空");
        parameter.populateEntity((resource) -> {
            Assert.INSTANCE.notNullElseRun(resource.getResource_id(), () -> resource.setResource_id(UidDb.INSTANCE.uid()));
            resource.initFields();
        });
        // 锁定判重
        return LockUtil.INSTANCE.executeInLock("RESOURCE_INSERT", () -> {
            ResourceQuery query = ResourceQuery.newInstance();
            query.setResource_key(resource_key);
            boolean exist = exist(query);
            Assert.INSTANCE.isFalse(exist, AssertLevel.PROMPT, "资源Key重复");
            return super.insert(parameter);
        });
    }

    @Override
    public boolean deleteByPrimaryKey(Long resource_id) {
        Assert.INSTANCE.notNull(resource_id, "resource_id不能为空");
        ResourceQuery query = ResourceQuery.newInstance();
        query.setResource_id(resource_id);
        return super.deleteByQuery(query) == 1;
    }

    @Override
    public boolean updateByPrimaryKey(ResourceParameterDomain parameter) {
        ResourceQuery query = ResourceQuery.newInstance();
        query.setResource_id(parameter.getResource_id());
        int rows = super.updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(ResourceParameterDomain parameter, ResourceQuery query) {
        return super.updateByQuery(parameter, query);
    }

    @Override
    public ResourceResultDomain byPrimaryKey(Long resource_id, String... returnFields) {
        Assert.INSTANCE.notNull(resource_id, "resource_id不能为空");
        ResourceQuery query = ResourceQuery.newInstance();
        query.setResource_id(resource_id);
        return super.one(query, returnFields);
    }

    @Override
    public ResourceResultDomain byPrimaryKeyOrThrow(Long resource_id, String... returnFields) {
        ResourceResultDomain resultDomain = byPrimaryKey(resource_id, returnFields);
        Assert.INSTANCE.notNull(resultDomain, "数据不存在或者状态错误");
        return resultDomain;
    }

    @Override
    public List<ResourceResultDomain> list(ResourceQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<ResourceResultDomain> pagination(ResourceQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

    @Override
    public Long idByKey(String resource_key) {
        Assert.INSTANCE.notBlank(resource_key, "资源Key不能为空");
        ResourceQuery query = ResourceQuery.newInstance();
        query.setResource_key(resource_key);
        ResourceResultDomain resourceResultDomain = one(query, "resource_id");
        Assert.INSTANCE.notNull(resourceResultDomain, "数据不存在或状态错误");
        return resourceResultDomain.getResource_id();
    }

    @Override
    public String keyById(Long resource_id) {
        Assert.INSTANCE.notNull(resource_id, "参数resource_id不能为空");
        ResourceResultDomain resultDomain = byPrimaryKeyOrThrow(resource_id, "resource_key");
        return resultDomain.getResource_key();
    }

}
