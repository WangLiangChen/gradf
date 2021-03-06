package liangchen.wang.gradf.component.foura.manager.impl;

import liangchen.wang.gradf.component.commons.base.AbstractManager;
import liangchen.wang.gradf.component.foura.dao.IUrlDao;
import liangchen.wang.gradf.component.foura.dao.entity.Url;
import liangchen.wang.gradf.component.foura.dao.query.UrlQuery;
import liangchen.wang.gradf.component.foura.manager.IUrlManager;
import liangchen.wang.gradf.component.foura.manager.domain.parameter.UrlParameterDomain;
import liangchen.wang.gradf.component.foura.manager.domain.result.UrlResultDomain;
import liangchen.wang.gradf.framework.commons.exception.PromptException;
import liangchen.wang.gradf.framework.commons.validator.Assert;
import liangchen.wang.gradf.framework.data.pagination.PaginationResult;
import liangchen.wang.gradf.framework.data.utils.UidDb;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author LiangChen.Wang 2020-04-13 00:40:41
 */
@Component("Gradf_Foura_DefaultUrlManager")
public class UrlManagerImpl extends AbstractManager<Url, UrlQuery, UrlResultDomain> implements IUrlManager {
    @Inject
    public UrlManagerImpl(@Named("Gradf_Foura_DefaultUrlDao") IUrlDao dao) {
        super("Url", "Url", dao);
    }

    @Override
    public boolean insert(UrlParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        parameter.populateEntity((url) -> {
            Assert.INSTANCE.notNullElseRun(url.getUrl_id(), () -> url.setUrl_id(UidDb.INSTANCE.uid()));
            url.initFields();
        });
        try {
            return super.insert(parameter);
        } catch (DuplicateKeyException e) {
            throw new PromptException("数据重复");
        }
    }

    @Override
    public boolean deleteByPrimaryKey(Long url_id) {
        Assert.INSTANCE.notNull(url_id, "url_id不能为空");
        UrlQuery query = UrlQuery.newInstance();
        query.setUrl_id(url_id);
        return super.deleteByQuery(query) == 1;
    }

    @Override
    public boolean updateByPrimaryKey(UrlParameterDomain parameter) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        UrlQuery query = UrlQuery.newInstance();
        query.setUrl_id(parameter.getUrl_id());
        int rows = updateByQuery(parameter, query);
        return rows == 1;
    }

    @Override
    public int updateByQuery(UrlParameterDomain parameter, UrlQuery query) {
        Assert.INSTANCE.notNull(parameter, "参数不能为空");
        Assert.INSTANCE.notNull(query, "查询参数不能为空");
        parameter.populateEntity((entity) -> {

        });
        return super.updateByQuery(parameter, query);
    }


    @Override
    public UrlResultDomain byPrimaryKey(Long url_id, String... returnFields) {
        Assert.INSTANCE.notNull(url_id, "参数不能为空");
        UrlQuery query = UrlQuery.newInstance();
        query.setUrl_id(url_id);
        return one(query, returnFields);
    }

    @Override
    public UrlResultDomain byPrimaryKeyOrThrow(Long url_id, String... returnFields) {
        UrlResultDomain resultDomain = byPrimaryKey(url_id, returnFields);
        Assert.INSTANCE.notNull(resultDomain, "数据不存在");
        return resultDomain;
    }


    @Override
    public List<UrlResultDomain> list(UrlQuery query, String... returnFields) {
        return super.list(query, returnFields);
    }

    @Override
    public PaginationResult<UrlResultDomain> pagination(UrlQuery query, String... returnFields) {
        return super.pagination(query, returnFields);
    }

    @Override
    public String urlPathById(Long url_id) {
        UrlResultDomain resultDomain = byPrimaryKeyOrThrow(url_id, "url_path");
        return resultDomain.getUrl_path();
    }

}
