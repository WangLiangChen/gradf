package liangchen.wang.gradf.component.foura.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.core.RootQuery;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import javax.persistence.Table;
import java.util.Set;

/**
 * @author LiangChen.Wang 2020-04-12 00:45:06
 */
@Table(name = "gradf_role")
public class RoleQuery extends RootQuery {
    private static final RoleQuery self = new RoleQuery();
    @Query(operator = Operator.EQUALS, column = "role_id")
    private Long role_id;
    @Query(operator = Operator.EQUALS, column = "role_key")
    private String role_key;
    @Query(operator = Operator.EQUALS, column = "status")
    private String status;
    @Query(operator = Operator.IN, column = "status")
    private String[] statusIn;
    @Query(operator = Operator.NOTIN, column = "status")
    private String[] statusNotIn;
    @Query(operator = Operator.IN, column = "role_id")
    private Set<Long> roleIdIn;

    public static RoleQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static RoleQuery newInstance(Long role_id) {
        RoleQuery query = newInstance();
        query.setRole_id(role_id);
        return query;
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

    public Set<Long> getRoleIdIn() {
        return roleIdIn;
    }

    public void setRoleIdIn(Set<Long> roleIdIn) {
        this.roleIdIn = roleIdIn;
    }

    public String getRole_key() {
        return role_key;
    }

    public void setRole_key(String role_key) {
        this.role_key = role_key;
    }
}
