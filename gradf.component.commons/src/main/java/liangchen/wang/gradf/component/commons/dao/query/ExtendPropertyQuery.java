package liangchen.wang.gradf.component.commons.dao.query;


import liangchen.wang.gradf.framework.commons.object.ClassBeanUtil;
import liangchen.wang.gradf.framework.data.annotation.Query;
import liangchen.wang.gradf.framework.data.base.RootQuery;
import liangchen.wang.gradf.framework.data.enumeration.Operator;

import javax.persistence.Table;

/**
 * @author LiangChen.Wang 2019-12-29 20:32:33
 */
@Table(name = "gradf_extend_property")
public class ExtendPropertyQuery extends RootQuery {
    private static final ExtendPropertyQuery self = new ExtendPropertyQuery();

    public static ExtendPropertyQuery newInstance() {
        return ClassBeanUtil.INSTANCE.cast(self.clone());
    }

    @Query(operator = Operator.EQUALS, column = "property_id")
    private Long property_id;


    public Long getProperty_id() {
        return property_id;
    }

    public void setProperty_id(Long property_id) {
        this.property_id = property_id;
    }

}
