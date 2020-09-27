package liangchen.wang.gradf.component.foura.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Entity(name = "crdf_account")
public class Account extends BaseEntity {
    private static final Account self = new Account();

    public static Account newInstance() {
        return ClassBeanUtil.INSTANCE.classCast(self.clone());
    }

    @Id
    private Long account_id;
    private String nick_name;
    private String login_password;
    private String password_salt;
    private String secret_key;
    private java.time.LocalDateTime expire_datetime;
    private java.time.LocalDateTime password_expire;
    private java.time.LocalDateTime last_login;
    private String last_ip;

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
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

    public String getPassword_salt() {
        return password_salt;
    }

    public void setPassword_salt(String password_salt) {
        this.password_salt = password_salt;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
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

}
