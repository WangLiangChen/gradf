package liangchen.wang.gradf.component.foura.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.core.RootQuery;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-03-07 10:24:36
 */
@Table(name = "gradf_operation_log")
public class OperationLogQuery extends RootQuery {
    private static final OperationLogQuery self = new OperationLogQuery();

    public static OperationLogQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static OperationLogQuery newInstance(Long log_id) {
        OperationLogQuery query = newInstance();
        query.setLog_id(log_id);
        return query;
    }

    @Query(operator = Operator.EQUALS, column = "log_id")
    private Long log_id;

    @Query(operator = Operator.EQUALS, column = "status")
    private String status;
    @Query(operator = Operator.IN, column = "status")
    private String[] statusIn;
    @Query(operator = Operator.NOTIN, column = "status")
    private String[] statusNotIn;
    @Query(operator = Operator.EQUALS, column = "business_id")
    private Long business_id;
    @Query(operator = Operator.EQUALS, column = "business_type")
    private String business_type;
    @Query(operator = Operator.EQUALS, column = "operation_flag")
    private String operation_flag;

    public Long getLog_id() {
        return log_id;
    }

    public void setLog_id(Long log_id) {
        this.log_id = log_id;
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

    public Long getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Long business_id) {
        this.business_id = business_id;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getOperation_flag() {
        return operation_flag;
    }

    public void setOperation_flag(String operation_flag) {
        this.operation_flag = operation_flag;
    }
}
