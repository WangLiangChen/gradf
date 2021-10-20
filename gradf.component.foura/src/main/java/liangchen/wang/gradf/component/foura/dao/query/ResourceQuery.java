package liangchen.wang.gradf.component.foura.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.query.RootQuery;
import liangchen.wang.gradf.framework.data.query.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Table(name = "gradf_resource")
public class ResourceQuery extends RootQuery {
    private static final ResourceQuery self = new ResourceQuery();
    @Query(operator = Operator.EQUALS, column = "resource_id")
    private Long resource_id;
    @Query(operator = Operator.EQUALS, column = "resource_key")
    private String resource_key;

    public static ResourceQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static ResourceQuery newInstance(Long resource_id) {
        ResourceQuery query = newInstance();
        query.setResource_id(resource_id);
        return query;
    }

    public Long getResource_id() {
        return resource_id;
    }

    public void setResource_id(Long resource_id) {
        this.resource_id = resource_id;
    }

    public String getResource_key() {
        return resource_key;
    }

    public void setResource_key(String resource_key) {
        this.resource_key = resource_key;
    }
}
