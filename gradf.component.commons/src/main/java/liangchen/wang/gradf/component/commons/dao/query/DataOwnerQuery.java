package liangchen.wang.gradf.component.commons.dao.query;


import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.query.RootQuery;
import liangchen.wang.gradf.framework.data.query.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2019-12-29 20:29:37
 */
@Table(name = "gradf_data_owner")
public class DataOwnerQuery extends RootQuery {
    private static final DataOwnerQuery self = new DataOwnerQuery();

    public static DataOwnerQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    @Query(operator = Operator.EQUALS, column = "record_id")
    private Long record_id;


    public Long getRecord_id() {
        return record_id;
    }

    public void setRecord_id(Long record_id) {
        this.record_id = record_id;
    }

}
