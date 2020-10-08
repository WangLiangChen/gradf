package liangchen.wang.gradf.component.foura.manager.domain.parameter;

import liangchen.wang.gradf.component.commons.base.ParameterDomain;
import liangchen.wang.gradf.component.foura.dao.entity.RoleAccount;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

import javax.validation.constraints.NotNull;

/**
 * @author LiangChen.Wang 2019-12-18 21:00:57
 */
public class RoleAccountParameterDomain extends ParameterDomain<RoleAccount> {
    private static final RoleAccountParameterDomain self = new RoleAccountParameterDomain();
    @NotNull(message = "角色ID不能为空")
    private Long role_id;
    @NotNull(message = "账户ID不能为空")
    private Long account_id;
    private String summary;
    private String status;

    public static RoleAccountParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
