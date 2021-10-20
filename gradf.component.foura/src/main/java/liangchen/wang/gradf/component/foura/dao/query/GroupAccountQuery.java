package liangchen.wang.gradf.component.foura.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.query.RootQuery;
import liangchen.wang.gradf.framework.data.query.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Table(name = "gradf_group_account")
public class GroupAccountQuery extends RootQuery {
    private static final GroupAccountQuery self = new GroupAccountQuery();
    @Query(operator = Operator.EQUALS, column = "group_id")
    private Long group_id;
    @Query(operator = Operator.EQUALS, column = "account_id")
    private Long account_id;
    @Query(operator = Operator.EQUALS, column = "status")
    private String status;
    @Query(operator = Operator.IN, column = "status")
    private String[] statusIn;
    @Query(operator = Operator.NOTIN, column = "status")
    private String[] statusNotIn;

    public static GroupAccountQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static GroupAccountQuery newInstance(Long group_id, Long account_id) {
        GroupAccountQuery query = newInstance();
        query.setGroup_id(group_id);
        query.setAccount_id(account_id);
        return query;
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
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
