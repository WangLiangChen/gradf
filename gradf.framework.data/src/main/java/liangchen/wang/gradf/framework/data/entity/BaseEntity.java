package liangchen.wang.gradf.framework.data.entity;

import liangchen.wang.gradf.framework.data.core.RootEntity;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @author LiangChen.Wang
 */
@MappedSuperclass
public class BaseEntity extends RootEntity {
    private Long sort;
    private LocalDateTime create_datetime;
    private LocalDateTime modify_datetime;
    private Long creator;
    private Long modifier;
    private String summary;
    private Byte data_mode;
    private String status;

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

    public Byte getData_mode() {
        return data_mode;
    }

    public void setData_mode(Byte data_mode) {
        this.data_mode = data_mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
