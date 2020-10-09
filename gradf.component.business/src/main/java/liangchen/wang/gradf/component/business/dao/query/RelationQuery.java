package liangchen.wang.gradf.component.business.dao.query;

import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.base.RootQuery;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2020-04-19 22:39:49
 */
@Table(name = "gradf_relation")
public class RelationQuery extends RootQuery {
    private static final RelationQuery self = new RelationQuery();

    public static RelationQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    public static RelationQuery newInstance(Long record_id) {
        RelationQuery query = newInstance();
        query.setRecord_id(record_id);
        return query;
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
