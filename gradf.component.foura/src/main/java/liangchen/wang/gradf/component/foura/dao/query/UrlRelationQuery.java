package liangchen.wang.gradf.component.foura.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.query.RootQuery;
import liangchen.wang.gradf.framework.data.query.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-07-29 13:56:01
 */
@Table(name = "gradf_url_relation")
public class UrlRelationQuery extends RootQuery {
    private static final UrlRelationQuery self = new UrlRelationQuery();
    @Query(operator = Operator.EQUALS, column = "role_id")
    private Long role_id;
    @Query(operator = Operator.EQUALS, column = "resource_id")
    private Long resource_id;
    @Query(operator = Operator.EQUALS, column = "operation_id")
    private Long operation_id;
    @Query(operator = Operator.EQUALS, column = "url_id")
    private Long url_id;

    public static UrlRelationQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static UrlRelationQuery newInstance(Long role_id, Long resource_id, Long operation_id, Long url_id) {
        UrlRelationQuery query = newInstance();
        query.setRole_id(role_id);
        query.setResource_id(resource_id);
        query.setOperation_id(operation_id);
        query.setUrl_id(url_id);
        return query;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public Long getResource_id() {
        return resource_id;
    }

    public void setResource_id(Long resource_id) {
        this.resource_id = resource_id;
    }

    public Long getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(Long operation_id) {
        this.operation_id = operation_id;
    }

    public Long getUrl_id() {
        return url_id;
    }

    public void setUrl_id(Long url_id) {
        this.url_id = url_id;
    }

}
