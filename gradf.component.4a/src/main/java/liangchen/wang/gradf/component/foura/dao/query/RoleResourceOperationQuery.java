package liangchen.wang.gradf.component.foura.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.base.RootQuery;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-04-12 23:55:49
*/
@Table(name = "gradf_role_resource_operation")
public class RoleResourceOperationQuery extends RootQuery {
    private static final RoleResourceOperationQuery self = new RoleResourceOperationQuery();

    public static RoleResourceOperationQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static RoleResourceOperationQuery newInstance(Long role_id, Long resource_id, Long operation_id) {
        RoleResourceOperationQuery query = newInstance();
        query.setRole_id(role_id);
        query.setResource_id(resource_id);
        query.setOperation_id(operation_id);
        return query;
    }

    @Query(operator = Operator.EQUALS, column = "role_id")
    private Long role_id;
    @Query(operator = Operator.EQUALS, column = "resource_id")
    private Long resource_id;
    @Query(operator = Operator.EQUALS, column = "operation_id")
    private Long operation_id;

    @Query(operator = Operator.EQUALS, column = "status")
    private String status;
    @Query(operator = Operator.IN, column = "status")
    private String[] statusIn;
    @Query(operator = Operator.NOTIN, column = "status")
    private String[] statusNotIn;

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

    public Long getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(Long operation_id) {
        this.operation_id = operation_id;
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
