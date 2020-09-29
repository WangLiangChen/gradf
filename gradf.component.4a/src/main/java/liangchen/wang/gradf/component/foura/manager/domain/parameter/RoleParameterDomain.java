package liangchen.wang.gradf.component.foura.manager.domain.parameter;

import liangchen.wang.gradf.component.web.base.ParameterDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.component.foura.dao.entity.Role;

import javax.validation.constraints.NotBlank;

/**
 * @author LiangChen.Wang 2019-12-18 21:01:23
 */
public class RoleParameterDomain extends ParameterDomain<Role> {
    private static final RoleParameterDomain self = new RoleParameterDomain();

    public static RoleParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    private Long role_id;
    @NotBlank(message = "角色Key不能为空")
    private String role_key;
    private String parent_key;
    @NotBlank(message = "角色名称不能为空")
    private String role_text;
    private Long sort;
    private String summary;
    private String status;

    public String getRole_key() {
        return role_key;
    }

    public void setRole_key(String role_key) {
        this.role_key = role_key;
    }

    public String getParent_key() {
        return parent_key;
    }

    public void setParent_key(String parent_key) {
        this.parent_key = parent_key;
    }

    public String getRole_text() {
        return role_text;
    }

    public void setRole_text(String role_text) {
        this.role_text = role_text;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }
}
