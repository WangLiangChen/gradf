package liangchen.wang.gradf.component.foura.manager.domain.result;

import liangchen.wang.gradf.component.web.base.ResultDomain;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author LiangChen.Wang 2019-12-16 14:55:41
 */
public class AccountResultDomain extends ResultDomain {
    private static final AccountResultDomain self = new AccountResultDomain();

    public static AccountResultDomain newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    private Long account_id;
    private String login_name;
    private String mobile;
    private String email;
    private String nick_name;
    private java.time.LocalDateTime expire_datetime;
    private java.time.LocalDateTime password_expire;
    private java.time.LocalDateTime last_login;
    private String last_ip;
    private Long sort;
    private java.time.LocalDateTime create_datetime;
    private java.time.LocalDateTime modify_datetime;
    private Long creator;
    private Long modifier;
    private String summary;
    private String status;

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

    public java.time.LocalDateTime getExpire_datetime() {
        return expire_datetime;
    }

    public void setExpire_datetime(java.time.LocalDateTime expire_datetime) {
        this.expire_datetime = expire_datetime;
    }

    public java.time.LocalDateTime getPassword_expire() {
        return password_expire;
    }

    public void setPassword_expire(java.time.LocalDateTime password_expire) {
        this.password_expire = password_expire;
    }

    public java.time.LocalDateTime getLast_login() {
        return last_login;
    }

    public void setLast_login(java.time.LocalDateTime last_login) {
        this.last_login = last_login;
    }

    public String getLast_ip() {
        return last_ip;
    }

    public void setLast_ip(String last_ip) {
        this.last_ip = last_ip;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
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
