package liangchen.wang.gradf.component.foura.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.base.RootQuery;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Table(name = "gradf_role_account")
public class RoleAccountQuery extends RootQuery {
    private static final RoleAccountQuery self = new RoleAccountQuery();
    @Query(operator = Operator.EQUALS, column = "role_id")
    private Long role_id;
    @Query(operator = Operator.EQUALS, column = "account_id")
    private Long account_id;
    @Query(operator = Operator.EQUALS, column = "status")
    private String status;
    @Query(operator = Operator.IN, column = "status")
    private String[] statusIn;
    @Query(operator = Operator.NOTIN, column = "status")
    private String[] statusNotIn;

    public static RoleAccountQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static RoleAccountQuery newInstance(Long role_id, Long account_id) {
        RoleAccountQuery query = newInstance();
        query.setRole_id(role_id);
        query.setAccount_id(account_id);
        return query;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getStatusIn() {
        return statusIn;
    }

    public void setStatusIn(String[] statusIn) {
        this.statusIn = statusIn;
    }

    public String[] getStatusNotIn() {
        return statusNotIn;
    }

    public void setStatusNotIn(String[] statusNotIn) {
        this.statusNotIn = statusNotIn;
    }
}
