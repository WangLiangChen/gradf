package liangchen.wang.gradf.component.foura.dao.entity;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.core.RootEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author LiangChen.Wang 2020-07-21 22:30:15
 */
@Entity(name = "gradf_account_login")
public class AccountLogin extends RootEntity {
    private static final AccountLogin self = new AccountLogin();
    @Id
    private String login_name;
    private Long account_id;
    private String login_mode;
    private String login_config;

    public static AccountLogin newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public String getLogin_mode() {
        return login_mode;
    }

    public void setLogin_mode(String login_mode) {
        this.login_mode = login_mode;
    }

    public String getLogin_config() {
        return login_config;
    }

    public void setLogin_config(String login_config) {
        this.login_config = login_config;
    }
}
