package liangchen.wang.gradf.component.foura.manager.domain.parameter;

import liangchen.wang.gradf.component.commons.base.ParameterDomain;
import liangchen.wang.gradf.component.foura.dao.entity.Group;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.validator.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author LiangChen.Wang 2020-04-12 01:44:04
 */
public class GroupParameterDomain extends ParameterDomain<Group> {
    private static final GroupParameterDomain self = new GroupParameterDomain();
    @NotNull(message = "群组ID不能为空", groups = {UpdateGroup.class})
    private Long group_id;
    private Long parent_id;
    private String group_key;
    @NotBlank(message = "群组名称不能为空")
    private String group_text;
    private Integer sort;
    private String summary;
    private String status;

    public static GroupParameterDomain newInstance() {
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
