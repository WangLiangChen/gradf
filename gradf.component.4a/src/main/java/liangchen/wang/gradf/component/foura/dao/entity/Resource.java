package liangchen.wang.gradf.component.foura.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.base.RootEntity;
import liangchen.wang.gradf.framework.data.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Entity(name = "gradf_resource")
public class Resource extends RootEntity {
    private static final Resource self = new Resource();

    public static Resource newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    @Id
    private Long resource_id;
    private Long parent_id;
    private String resource_key;
    private String resource_text;
    private Byte auth_type;
    private LocalDateTime create_datetime;
    private String summary;

    public Long getResource_id() {
        return resource_id;
    }

    public void setResource_id(Long resource_id) {
        this.resource_id = resource_id;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public String getResource_key() {
        return resource_key;
    }

    public void setResource_key(String resource_key) {
        this.resource_key = resource_key;
    }

    public String getResource_text() {
        return resource_text;
    }

    public void setResource_text(String resource_text) {
        this.resource_text = resource_text;
    }

    public Byte getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(Byte auth_type) {
        this.auth_type = auth_type;
    }

    public LocalDateTime getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(LocalDateTime create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
