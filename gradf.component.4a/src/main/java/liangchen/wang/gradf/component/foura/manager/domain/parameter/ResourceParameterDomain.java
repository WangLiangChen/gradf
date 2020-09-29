package liangchen.wang.gradf.component.foura.manager.domain.parameter;

import liangchen.wang.gradf.component.web.base.ParameterDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.component.foura.dao.entity.Resource;

import javax.validation.constraints.NotBlank;

/**
 * @author LiangChen.Wang 2019-12-18 21:02:21
 */
public class ResourceParameterDomain extends ParameterDomain<Resource> {
    private static final ResourceParameterDomain self = new ResourceParameterDomain();

    public static ResourceParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    private Long resource_id;
    @NotBlank(message = "资源Key不能为空")
    private String resource_key;
    private Long parent_id;
    @NotBlank(message = "资源名称不能为空")
    private String resource_text;
    private Byte auth_type;
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


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
