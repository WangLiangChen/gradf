package liangchen.wang.gradf.component.foura.manager.domain.result;


import liangchen.wang.gradf.framework.commons.object.EnhancedObject;

import java.time.LocalDateTime;

/**
 * @author LiangChen.Wang
 */
public class AccountPasswordResultDomain extends EnhancedObject {
    private static final long serialVersionUID = 1475736915931668482L;

    public static AccountPasswordResultDomain newInstance() {
        return new AccountPasswordResultDomain();
    }

    private Long account_id;
    private String login_name;
    private String nick_name;
    private String mobile;
    private String email;
    private String login_password;
    private String password_salt;
    private String jwt_key;
    private LocalDateTime expire_datetime;
    private LocalDateTime password_expire;

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

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
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

    public String getLogin_password() {
        return login_password;
    }

    public void setLogin_password(String login_password) {
        this.login_password = login_password;
    }

    public String getPassword_salt() {
        return password_salt;
    }

    public void setPassword_salt(String password_salt) {
        this.password_salt = password_salt;
    }

    public String getJwt_key() {
        return jwt_key;
    }

    public void setJwt_key(String jwt_key) {
        this.jwt_key = jwt_key;
    }

    public LocalDateTime getExpire_datetime() {
        return expire_datetime;
    }

    public void setExpire_datetime(LocalDateTime expire_datetime) {
        this.expire_datetime = expire_datetime;
    }

    public LocalDateTime getPassword_expire() {
        return password_expire;
    }

    public void setPassword_expire(LocalDateTime password_expire) {
        this.password_expire = password_expire;
    }
}
