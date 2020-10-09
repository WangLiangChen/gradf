package liangchen.wang.gradf.component.business.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.base.RootQuery;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-03-24 08:35:30
*/
@Table(name = "gradf_address")
public class AddressQuery extends RootQuery {
    private static final AddressQuery self = new AddressQuery();

    public static AddressQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static AddressQuery newInstance(Long address_id) {
        AddressQuery query = newInstance();
        query.setAddress_id(address_id);
        return query;
    }

    @Query(operator = Operator.EQUALS, column = "address_id")
    private Long address_id;

    @Query(operator = Operator.EQUALS, column = "status")
    private String status;
    @Query(operator = Operator.IN, column = "status")
    private String[] statusIn;
    @Query(operator = Operator.NOTIN, column = "status")
    private String[] statusNotIn;

    public Long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Long address_id) {
        this.address_id = address_id;
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
