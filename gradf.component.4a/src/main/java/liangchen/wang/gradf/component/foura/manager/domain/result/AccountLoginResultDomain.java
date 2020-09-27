package liangchen.wang.gradf.component.foura.manager.domain.result;

import java.time.LocalDateTime;
import java.util.List;

public class AccountLoginResultDomain {
    public static AccountLoginResultDomain newInstance() {
        return new AccountLoginResultDomain();
    }

    private Long account_id;
    private String access_token;
    private String login_name;
    private String mobile;
    private String email;
    private String nick_name;
    private LocalDateTime last_login;
    private List<Role> roles;

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LocalDateTime getLast_login() {
        return last_login;
    }

    public void setLast_login(LocalDateTime last_login) {
        this.last_login = last_login;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public static class Role {
        private String role_key;
        private String role_text;

        public Role(String role_key, String role_text) {
            this.role_key = role_key;
            this.role_text = role_text;
        }

        public String getRole_key() {
            return role_key;
        }

        public void setRole_key(String role_key) {
            this.role_key = role_key;
        }

        public String getRole_text() {
            return role_text;
        }

        public void setRole_text(String role_text) {
            this.role_text = role_text;
        }
    }
}
