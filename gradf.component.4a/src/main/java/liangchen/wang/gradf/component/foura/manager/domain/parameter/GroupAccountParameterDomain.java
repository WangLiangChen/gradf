package liangchen.wang.gradf.component.foura.manager.domain.parameter;

import liangchen.wang.gradf.component.web.base.ParameterDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.component.foura.dao.entity.GroupAccount;

import javax.validation.constraints.NotNull;

/**
 * @author LiangChen.Wang 2019-12-18 20:59:37
 */
public class GroupAccountParameterDomain extends ParameterDomain<GroupAccount> {
    private static final GroupAccountParameterDomain self = new GroupAccountParameterDomain();

    public static GroupAccountParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    @NotNull(message = "群组ID不能为空")
    private Long group_id;
    @NotNull(message = "账户ID不能为空")
    private Long account_id;
    private String status;
    private String summary;

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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
