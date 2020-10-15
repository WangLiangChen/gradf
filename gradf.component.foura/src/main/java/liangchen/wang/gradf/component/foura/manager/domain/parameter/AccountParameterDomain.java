package liangchen.wang.gradf.component.foura.manager.domain.parameter;

import liangchen.wang.gradf.component.commons.base.ParameterDomain;
import liangchen.wang.gradf.component.foura.dao.entity.Account;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.commons.validator.UpdateGroup;

import javax.validation.constraints.NotNull;

/**
 * @author LiangChen.Wang 2019-12-16 14:55:40
 */
public class AccountParameterDomain extends ParameterDomain<Account> {
    private static final AccountParameterDomain self = new AccountParameterDomain();
    @NotNull(message = "账户ID不能为空", groups = {UpdateGroup.class})
    private Long account_id;
    private String login_name;
    private String mobile;
    private String email;
    private String nick_name;
    private String login_password;
    private Long sort;
    private String summary;
    private String status;
    private Byte data_mode;

    public static AccountParameterDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getLogin_password() {
        return login_password;
    }

    public void setLogin_password(String login_password) {
        this.login_password = login_password;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
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

    public void setData_mode(Byte data_mode) {
        this.data_mode = data_mode;
    }

    public Byte getData_mode() {
        return data_mode;
    }
}
