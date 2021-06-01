package liangchen.wang.gradf.framework.data.entity;

import liangchen.wang.gradf.framework.data.core.RootEntity;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * @author LiangChen.Wang
 */
@MappedSuperclass
public class BaseEntity extends RootEntity {
    private Integer sort;
    private LocalDateTime create_datetime;
    private LocalDateTime modify_datetime;
    private String creator;
    private String modifier;
    private Byte data_mode;
    private String state;
    private String summary;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Byte getData_mode() {
        return data_mode;
    }

    public void setData_mode(Byte data_mode) {
        this.data_mode = data_mode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
