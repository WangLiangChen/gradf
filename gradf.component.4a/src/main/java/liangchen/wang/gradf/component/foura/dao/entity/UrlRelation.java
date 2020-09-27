package liangchen.wang.gradf.component.foura.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.base.RootEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author LiangChen.Wang 2020-07-29 13:56:01
*/
@Entity(name = "crdf_url_relation")
public class UrlRelation extends RootEntity {
    private static final UrlRelation self = new UrlRelation();

    public static UrlRelation newInstance() {
        return ClassBeanUtil.INSTANCE.classCast(self.clone());
    }

    @Id
    private Long role_id;
    @Id
    private Long resource_id;
    @Id
    private Long operation_id;
    @Id
    private Long url_id;

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
