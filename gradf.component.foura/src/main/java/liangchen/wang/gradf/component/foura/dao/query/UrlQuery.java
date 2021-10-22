package liangchen.wang.gradf.component.foura.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.query.RootQuery;
import liangchen.wang.gradf.framework.data.query.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-04-13 00:40:41
 */
@Table(name = "gradf_url")
public class UrlQuery extends RootQuery {
    private static final UrlQuery self = new UrlQuery();
    @Query(operator = Operator.EQUALS, column = "url_id")
    private Long url_id;

    public static UrlQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static UrlQuery newInstance(Long url_id) {
        UrlQuery query = newInstance();
        query.setUrl_id(url_id);
        return query;
    }

    public Long getUrl_id() {
        return url_id;
    }

    public void setUrl_id(Long url_id) {
        this.url_id = url_id;
    }

}
