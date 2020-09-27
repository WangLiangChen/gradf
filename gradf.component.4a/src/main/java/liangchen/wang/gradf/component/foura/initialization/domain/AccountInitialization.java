package liangchen.wang.gradf.component.foura.initialization.domain;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;

/**
 * @author .LiangChen.Wang
 */
public class AccountInitialization extends FouraInitialization {
    private static final AccountInitialization self = new AccountInitialization();

    public static AccountInitialization newInstance(String loginName,String nickName,String password) {
        AccountInitialization accountInitialization = ClassBeanUtil.INSTANCE.classCast(self.clone());
        accountInitialization.loginName = loginName;
        accountInitialization.nickName = nickName;
        accountInitialization.password = password;
        return accountInitialization;
    }

    private String loginName;
    private String mobile;
    private String email;
    private String nickName;
    private String password;

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
}
