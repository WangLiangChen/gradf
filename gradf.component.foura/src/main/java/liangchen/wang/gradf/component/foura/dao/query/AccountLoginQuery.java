package liangchen.wang.gradf.component.foura.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.core.RootQuery;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-07-21 22:30:15
 */
@Table(name = "gradf_account_login")
public class AccountLoginQuery extends RootQuery {
    private static final AccountLoginQuery self = new AccountLoginQuery();
    @Query(operator = Operator.EQUALS, column = "login_name")
    private String login_name;

    public static AccountLoginQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static AccountLoginQuery newInstance(String login_name) {
        AccountLoginQuery query = newInstance();
        query.setLogin_name(login_name);
        return query;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

}
