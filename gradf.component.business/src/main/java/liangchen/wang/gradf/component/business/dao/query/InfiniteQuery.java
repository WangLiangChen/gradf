package liangchen.wang.gradf.component.business.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.core.RootQuery;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-05-12 19:37:27
 */
@Table(name = "gradf_infinite")
public class InfiniteQuery extends RootQuery {
    private static final InfiniteQuery self = new InfiniteQuery();

    public static InfiniteQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static InfiniteQuery newInstance(Long infinite_id) {
        InfiniteQuery query = newInstance();
        query.setInfinite_id(infinite_id);
        return query;
    }

    @Query(operator = Operator.EQUALS, column = "infinite_id")
    private Long infinite_id;

    @Query(operator = Operator.EQUALS, column = "status")
    private String status;
    @Query(operator = Operator.NOTEQUALS, column = "status")
    private String statusNotEquals;
    @Query(operator = Operator.IN, column = "status")
    private String[] statusIn;
    @Query(operator = Operator.NOTIN, column = "status")
    private String[] statusNotIn;
    @Query(operator = Operator.EQUALS, column = "business_flag")
    private String business_flag;
    @Query(operator = Operator.EQUALS, column = "infinite_flag")
    private String infinite_flag;

    public Long getInfinite_id() {
        return infinite_id;
    }

    public void setInfinite_id(Long infinite_id) {
        this.infinite_id = infinite_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusNotEquals() {
        return statusNotEquals;
    }

    public void setStatusNotEquals(String statusNotEquals) {
        this.statusNotEquals = statusNotEquals;
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

    public void setBusiness_flag(String business_flag) {
        this.business_flag = business_flag;
    }

    public String getBusiness_flag() {
        return business_flag;
    }

    public void setInfinite_flag(String infinite_flag) {
        this.infinite_flag = infinite_flag;
    }

    public String getInfinite_flag() {
        return infinite_flag;
    }
}
