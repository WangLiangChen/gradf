package liangchen.wang.gradf.component.foura.manager.domain.result;

import liangchen.wang.gradf.component.web.base.ResultDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author LiangChen.Wang 2019-12-18 21:02:21
 */
public class ResourceResultDomain extends ResultDomain {
    private static final ResourceResultDomain self = new ResourceResultDomain();

    public static ResourceResultDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    private Long resource_id;
    private Long parent_id;
    private String resource_key;
    private String resource_text;
    private Byte auth_type;
    private java.time.LocalDateTime create_datetime;
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


    public java.time.LocalDateTime getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(java.time.LocalDateTime create_datetime) {
        this.create_datetime = create_datetime;
    }


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
