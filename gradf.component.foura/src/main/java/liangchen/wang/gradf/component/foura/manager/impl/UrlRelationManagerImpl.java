package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.component.foura.dao.IUrlRelationDao;
import liangchen.wang.gradf.component.foura.dao.entity.UrlRelation;
import liangchen.wang.gradf.component.foura.dao.query.UrlRelationQuery;
import liangchen.wang.gradf.component.foura.manager.IUrlRelationManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.UrlRelationParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.UrlRelationResultDomain;
import liangchen.wang.gradf.framework.commons.exception.PromptException;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author LiangChen.Wang 2020-07-29 13:56:01
 */
@Component("Gradf_Foura_DefaultUrlRelationManager")
public class UrlRelationManagerImpl extends AbstractManager<UrlRelation, UrlRelationQuery, UrlRelationResultDomain> implements IUrlRelationManager {
    @Inject
    public UrlRelationManagerImpl(@Named("Gradf_Foura_DefaultUrlRelationDao") IUrlRelationDao dao) {
        super("Url关系", "UrlRelation", dao);
    }

    @Override
    public boolean insert(UrlRelationParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        parameter.populateEntity((urlRelation) -> {
            urlRelation.initFields();
        });
        try {
            return super.insert(parameter);
        } catch (DuplicateKeyException e) {
            throw new PromptException("数据重复");
        }

    }

    @Override
    public List<UrlRelationResultDomain> list(UrlRelationQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<UrlRelationResultDomain> pagination(UrlRelationQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

}
