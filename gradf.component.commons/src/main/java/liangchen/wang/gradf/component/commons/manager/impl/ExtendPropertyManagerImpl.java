package liangchen.wang.gradf.component.commons.manager.impl;

import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.component.commons.dao.IExtendPropertyDao;
import liangchen.wang.gradf.component.commons.dao.entity.ExtendProperty;
import liangchen.wang.gradf.component.commons.dao.query.ExtendPropertyQuery;
import liangchen.wang.gradf.component.commons.manager.IExtendPropertyManager;
import liangchen.wang.gradf.component.commons.manager.domain.parameter.ExtendPropertyParameterDomain;
import liangchen.wang.gradf.component.commons.manager.domain.result.ExtendPropertyResultDomain;
import liangchen.wang.gradf.framework.commons.exception.InfoException;
import liangchen.wang.gradf.framework.commons.utils.CollectionUtil;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author LiangChen.Wang 2019-12-29 20:32:33
 */
@Component("Gradf_Business_DefaultExtendPropertyManager")
public class ExtendPropertyManagerImpl extends AbstractManager<ExtendProperty, ExtendPropertyQuery, ExtendPropertyResultDomain> implements IExtendPropertyManager {
    @Inject
    public ExtendPropertyManagerImpl(@Named("Gradf_Business_DefaultExtendPropertyDao") IExtendPropertyDao dao) {
        super("扩展属性", "ExtendProperty", dao);
    }

    @Override
    public boolean insert(ExtendPropertyParameterDomain parameter) {
        parameter.populateEntity((entity) -> {
            ExtendProperty extendProperty = (ExtendProperty) entity;
            //TODO 这里可以调整Entity，比如设置主键/状态等
        });
        return super.insert(parameter);
    }

    @Override
    public boolean deleteByPrimaryKey(Long property_id) {
        ExtendPropertyQuery query = ExtendPropertyQuery.newInstance();
        query.setProperty_id(property_id);
        int rows = super.deleteByQuery(query);
        return rows == 1;
    }

    @Override
    public int deleteByQuery(ExtendPropertyQuery query) {
        return super.deleteByQuery(query);
    }

    @Override
    public boolean updateByPrimaryKey(ExtendPropertyParameterDomain parameter) {
        ExtendPropertyQuery query = ExtendPropertyQuery.newInstance();
        query.setProperty_id(parameter.getProperty_id());
        int rows = super.updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(ExtendPropertyParameterDomain parameter, ExtendPropertyQuery query) {
        return super.updateByQuery(parameter, query);
    }

    @Override
    public ExtendPropertyResultDomain byPrimaryKey(Long property_id) {
        ExtendPropertyQuery query = ExtendPropertyQuery.newInstance();
        query.setProperty_id(property_id);
        List<ExtendPropertyResultDomain> list = super.list(query);
        if (CollectionUtil.INSTANCE.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public ExtendPropertyResultDomain byPrimaryKeyOrThrow(Long property_id) {
        ExtendPropertyResultDomain resultDomain = byPrimaryKey(property_id);
        if (null == resultDomain) {
            throw new InfoException("数据不存在");
        }
        return resultDomain;
    }

    @Override
    public List<ExtendPropertyResultDomain> list(ExtendPropertyQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<ExtendPropertyResultDomain> pagination(ExtendPropertyQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

}
