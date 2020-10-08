package liangchen.wang.gradf.component.foura.initialization.domain;

import liangchen.wang.gradf.framework.commons.digest.HashUtil;
import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

import java.util.Objects;

/**
 * @author .LiangChen.Wang
 */
public class AccountInitialization extends FouraInitialization {
    private static final AccountInitialization self = new AccountInitialization();
    private String loginName;
    private String mobile;
    private String email;
    private String nickName;
    private String password;

    public static AccountInitialization newInstance(String loginName, String nickName, String password) {
        AccountInitialization accountInitialization = ClassBeanUtil.INSTANCE.cast(self.clone());
        accountInitialization.loginName = loginName;
        accountInitialization.nickName = nickName;
        accountInitialization.password = password;
        return accountInitialization;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int hashCode() {
        return HashUtil.INSTANCE.hashCode(loginName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AccountInitialization)) {
            return false;
        }
        AccountInitialization other = (AccountInitialization) obj;
        return Objects.equals(this.loginName, other.getLoginName());
    }
}
