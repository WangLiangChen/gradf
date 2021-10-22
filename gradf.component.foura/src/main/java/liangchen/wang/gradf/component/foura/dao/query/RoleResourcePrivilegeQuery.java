package liangchen.wang.gradf.component.foura.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.query.RootQuery;
import liangchen.wang.gradf.framework.data.query.Operator;

import javax.persistence.Table;
import java.util.Set;

/**
 * @author LiangChen.Wang 2020-04-12 23:55:23
 */
@Table(name = "gradf_role_resource_privilege")
public class RoleResourcePrivilegeQuery extends RootQuery {
    private static final RoleResourcePrivilegeQuery self = new RoleResourcePrivilegeQuery();
    @Query(operator = Operator.EQUALS, column = "role_id")
    private Long role_id;
    @Query(operator = Operator.EQUALS, column = "resource_id")
    private Long resource_id;
    @Query(operator = Operator.EQUALS, column = "status")
    private String status;
    @Query(operator = Operator.IN, column = "status")
    private String[] statusIn;
    @Query(operator = Operator.NOTIN, column = "status")
    private String[] statusNotIn;
    @Query(operator = Operator.NOTIN, column = "role_id")
    private Set<Long> roleIdIn;

    public static RoleResourcePrivilegeQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static RoleResourcePrivilegeQuery newInstance(Long role_id, Long resource_id) {
        RoleResourcePrivilegeQuery query = newInstance();
        query.setRole_id(role_id);
        query.setResource_id(resource_id);
        return query;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public Long getResource_id() {
        return resource_id;
    }

    public void setResource_id(Long resource_id) {
        this.resource_id = resource_id;
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

    public void setRoleIdIn(Set<Long> roleIdIn) {
        this.roleIdIn = roleIdIn;
    }

    public Set<Long> getRoleIdIn() {
        return roleIdIn;
    }
}
