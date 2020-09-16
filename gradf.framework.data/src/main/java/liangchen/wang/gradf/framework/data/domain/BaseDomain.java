package liangchen.wang.gradf.framework.data.domain;

import liangchen.wang.crdf.framework.commons.pagination.PaginationParameter;

import java.time.LocalDateTime;

/**
 * @author LiangChen.Wang
 */
public class BaseDomain extends PaginationParameter {
    private Long sort;
    private Long creator;
    private Long modifier;
    private LocalDateTime create_datetime;
    private LocalDateTime modify_datetime;
    private String summary;
    private Integer data_mode;
    private String status;

    private String creator_name;
    private String modifier_name;
    private String status_text;
    private String data_mode_text;

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getData_mode() {
        return data_mode;
    }

    public void setData_mode(Integer data_mode) {
        this.data_mode = data_mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public String getModifier_name() {
        return modifier_name;
    }

    public void setModifier_name(String modifier_name) {
        this.modifier_name = modifier_name;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public String getData_mode_text() {
        return data_mode_text;
    }

    public void setData_mode_text(String data_mode_text) {
        this.data_mode_text = data_mode_text;
    }
}
