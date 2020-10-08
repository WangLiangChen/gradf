package liangchen.wang.gradf.component.foura.manager.domain.result;

import liangchen.wang.gradf.component.commons.base.ResultDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

import java.time.LocalDateTime;

/**
 * @author LiangChen.Wang 2019-12-18 20:59:37
 */
public class GroupAccountResultDomain extends ResultDomain {
    private static final GroupAccountResultDomain self = new GroupAccountResultDomain();
    private Long group_id;
    private Long account_id;
    private LocalDateTime create_datetime;
    private LocalDateTime modify_datetime;
    private String summary;
    private String status;

    public static GroupAccountResultDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }
}
