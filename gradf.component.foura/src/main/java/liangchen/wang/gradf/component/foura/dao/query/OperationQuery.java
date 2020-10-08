package liangchen.wang.gradf.component.foura.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.base.RootQuery;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-04-12 23:47:02
 */
@Table(name = "gradf_operation")
public class OperationQuery extends RootQuery {
    private static final OperationQuery self = new OperationQuery();
    @Query(operator = Operator.EQUALS, column = "operation_id")
    private Long operation_id;

    public static OperationQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static OperationQuery newInstance(Long operation_id) {
        OperationQuery query = newInstance();
        query.setOperation_id(operation_id);
        return query;
    }

    public Long getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(Long operation_id) {
        this.operation_id = operation_id;
    }

}
