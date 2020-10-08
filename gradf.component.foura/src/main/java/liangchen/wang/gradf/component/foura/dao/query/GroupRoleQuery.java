package liangchen.wang.gradf.component.foura.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.base.RootQuery;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-10-08 19:11:17
*/
@Table(name = "gradf_group_role")
public class GroupRoleQuery extends RootQuery {
    private static final GroupRoleQuery self = new GroupRoleQuery();

    public static GroupRoleQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static GroupRoleQuery newInstance(Long group_id, Long role_id) {
        GroupRoleQuery query = newInstance();
        query.setGroup_id(group_id);
        query.setRole_id(role_id);
        return query;
    }

    @Query(operator = Operator.EQUALS, column = "group_id")
    private Long group_id;
    @Query(operator = Operator.EQUALS, column = "role_id")
    private Long role_id;

    @Query(operator = Operator.EQUALS, column = "status")
    private String status;
    @Query(operator = Operator.IN, column = "status")
    private String[] statusIn;
    @Query(operator = Operator.NOTIN, column = "status")
    private String[] statusNotIn;

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
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
