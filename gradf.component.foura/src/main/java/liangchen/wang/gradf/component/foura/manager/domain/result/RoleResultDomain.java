package liangchen.wang.gradf.component.foura.manager.domain.result;

import liangchen.wang.gradf.component.commons.base.ResultDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

import java.time.LocalDateTime;

/**
 * @author LiangChen.Wang 2019-12-18 21:01:23
 */
public class RoleResultDomain extends ResultDomain {
    private static final RoleResultDomain self = new RoleResultDomain();
    private Long role_id;
    private String role_key;
    private String parent_key;
    private String role_text;
    private Long sort;
    private LocalDateTime create_datetime;
    private LocalDateTime modify_datetime;
    private Long creator;
    private Long modifier;
    private String summary;
    private String status;

    public static RoleResultDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

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

    public LocalDateTime getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(LocalDateTime create_datetime) {
        this.create_datetime = create_datetime;
    }

    public LocalDateTime getModify_datetime() {
        return modify_datetime;
    }

    public void setModify_datetime(LocalDateTime modify_datetime) {
        this.modify_datetime = modify_datetime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Long getModifier() {
        return modifier;
    }

    public void setModifier(Long modifier) {
        this.modifier = modifier;
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
