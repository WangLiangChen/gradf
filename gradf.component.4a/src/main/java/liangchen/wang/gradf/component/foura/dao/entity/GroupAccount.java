package liangchen.wang.gradf.component.foura.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.base.RootEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
*/
@Entity(name = "gradf_group_account")
public class GroupAccount extends RootEntity {
    private static final GroupAccount self = new GroupAccount();

    public static GroupAccount newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    @Id
    private Long group_id;
    @Id
    private Long account_id;
    private java.time.LocalDateTime create_datetime;
    private java.time.LocalDateTime modify_datetime;
    private Long creator;
    private Long modifier;
    private String summary;
    private String status;

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
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
