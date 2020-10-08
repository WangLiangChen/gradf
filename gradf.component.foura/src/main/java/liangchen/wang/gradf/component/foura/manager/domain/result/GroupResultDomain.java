package liangchen.wang.gradf.component.foura.manager.domain.result;

import liangchen.wang.gradf.component.commons.base.ResultDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author LiangChen.Wang 2020-04-12 01:44:04
 */
public class GroupResultDomain extends ResultDomain {
    private static final GroupResultDomain self = new GroupResultDomain();
    private Long group_id;
    private Long parent_id;
    private String group_key;
    private String group_text;
    private Integer sort;
    private java.time.LocalDateTime create_datetime;
    private java.time.LocalDateTime modify_datetime;
    private Long creator;
    private Long modifier;
    private String summary;
    private String status;

    public static GroupResultDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    public String getGroup_key() {
        return group_key;
    }

    public void setGroup_key(String group_key) {
        this.group_key = group_key;
    }

    public String getGroup_text() {
        return group_text;
    }

    public void setGroup_text(String group_text) {
        this.group_text = group_text;
    }


    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public java.time.LocalDateTime getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(java.time.LocalDateTime create_datetime) {
        this.create_datetime = create_datetime;
    }

    public java.time.LocalDateTime getModify_datetime() {
        return modify_datetime;
    }

    public void setModify_datetime(java.time.LocalDateTime modify_datetime) {
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

}
